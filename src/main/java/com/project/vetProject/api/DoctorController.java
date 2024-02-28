package com.project.vetProject.api;

import com.project.vetProject.business.abstracts.IDoctorService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.doctor.DoctorSaveRequest;
import com.project.vetProject.dto.request.doctor.DoctorUpdateRequest;
import com.project.vetProject.dto.response.doctor.DoctorResponse;
import com.project.vetProject.entity.Doctor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;

@RestController
@RequestMapping("/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final IDoctorService doctorService;
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequest doctorSaveRequest){
        return this.doctorService.save(doctorSaveRequest);
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest){
        return this.doctorService.update(doctorUpdateRequest);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<DoctorResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return this.doctorService.cursor(page, pageSize);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.doctorService.delete(id);
        return ResultHelper.ok();
    }
}
