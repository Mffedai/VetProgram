package com.project.vetProject.api;

import com.project.vetProject.business.abstracts.IAvailableDateService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.availableDate.AvailableDateSaveRequest;
import com.project.vetProject.dto.request.availableDate.AvailableDateUpdateRequest;
import com.project.vetProject.dto.response.availableDate.AvailableDateResponse;
import com.project.vetProject.entity.AvailableDate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/available-dates")
public class AvailableDateController {
    private final IAvailableDateService availableDateService;
    private final IModelMapperService modelMapperService;

    public AvailableDateController(IAvailableDateService availableDateService, IModelMapperService modelMapperService) {
        this.availableDateService = availableDateService;
        this.modelMapperService = modelMapperService;
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest availableDateSaveRequest){
        AvailableDate saveAvailableDate = this.modelMapperService.forRequest().map(availableDateSaveRequest, AvailableDate.class);
        this.availableDateService.save(saveAvailableDate);
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveAvailableDate, AvailableDateResponse.class));
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        Page<AvailableDate> availableDatePage = this.availableDateService.cursor(page, pageSize);
        Page<AvailableDateResponse> availableDateResponsePage = availableDatePage.map(availableDate -> this.modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class));
        return ResultHelper.cursor(availableDateResponsePage);
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest){
        this.availableDateService.get(availableDateUpdateRequest.getId());
        AvailableDate updateAvailableDate = this.modelMapperService.forRequest().map(availableDateUpdateRequest, AvailableDate.class);
        this.availableDateService.update(updateAvailableDate);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAvailableDate, AvailableDateResponse.class));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.availableDateService.delete(id);
        return ResultHelper.ok();
    }
}
