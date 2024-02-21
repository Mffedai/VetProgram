package com.project.vetProject.api;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.IAppointmentService;
import com.project.vetProject.business.abstracts.IDoctorService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.result.Result;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dto.CursorResponse;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.request.appointment.AppointmentUpdateRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Appointment;
import com.project.vetProject.entity.Doctor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;
    private final IModelMapperService modelMapperService;


    public AppointmentController(IAppointmentService appointmentService, IModelMapperService modelMapperService) {
        this.appointmentService = appointmentService;
        this.modelMapperService = modelMapperService;

    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){
        return this.appointmentService.save(appointmentSaveRequest);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        Page<Appointment> appointmentPage = this.appointmentService.cursor(page, pageSize);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage.map(appointment -> this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));
        return ResultHelper.cursor(appointmentResponsePage);
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest){
        this.appointmentService.get(appointmentUpdateRequest.getId());
        Appointment updateAppointment = this.modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);
        this.appointmentService.update(updateAppointment);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAppointment, AppointmentResponse.class));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable int id){
        this.appointmentService.delete(id);
        return ResultHelper.ok();
    }
    @GetMapping("/filterByDrDate/{doctorId}-{findByDate}")
    public List<Appointment> getDoctorIdAndDate(
            @PathVariable("doctorId") int id,
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exitDate
    ){
        LocalDateTime convertedEntryDate = entryDate.atStartOfDay();
        LocalDateTime convertedExitDate = exitDate.atStartOfDay().plusDays(1);
        return this.appointmentService.findByDoctorIdAndDateTimeBetween(id,convertedEntryDate,convertedExitDate);
    }

    @GetMapping("/filterByAnmlDate/{animalId}-{findByDate}")
    public List<Appointment> getAnimalIdAndDate(
            @PathVariable("animalId") int id,
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate exitDate
    ){
        LocalDateTime convertedEntryDate = entryDate.atStartOfDay();
        LocalDateTime convertedExitDate = exitDate.atStartOfDay().plusDays(1);
        return this.appointmentService.findByAnimalIdAndDateTimeBetween(id, convertedEntryDate, convertedExitDate);
    }

}
