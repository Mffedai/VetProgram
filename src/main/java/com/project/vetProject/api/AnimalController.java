package com.project.vetProject.api;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.ICustomerService;
import com.project.vetProject.core.config.ConvertEntityToResponse;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.animal.AnimalSaveRequest;
import com.project.vetProject.dto.request.animal.AnimalUpdateRequest;
import com.project.vetProject.dto.response.animal.AnimalResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Customer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/animals")
@RequiredArgsConstructor
public class AnimalController {
    private final IAnimalService animalService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest){
        return this.animalService.save(animalSaveRequest);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        return this.animalService.cursor(page, pageSize);
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest){
        return this.animalService.update(animalUpdateRequest);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.animalService.delete(id);
        return ResultHelper.ok();
    }
    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> get(@PathVariable("name") String name){
        return this.animalService.findByName(name);
    }
    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getAnimalsByCustomerId(@PathVariable("id") int customerId){
        return this.animalService.findByCustomerId(customerId);
    }
}
