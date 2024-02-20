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
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {
    private final IAppointmentService appointmentService;
    private final IModelMapperService modelMapperService;
    private final IDoctorService doctorService;
    private final IAnimalService animalService;

    public AppointmentController(IAppointmentService appointmentService, IModelMapperService modelMapperService, IDoctorService doctorService, IAnimalService animalService) {
        this.appointmentService = appointmentService;
        this.modelMapperService = modelMapperService;
        this.doctorService = doctorService;
        this.animalService = animalService;
    }
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest){

        //LocalDateTime dakika ve saniyeleri 0'layıp sadece saat bilgisine göre setleniyor.
        LocalDateTime dateTime = appointmentSaveRequest.getDateTime();
        dateTime = dateTime.withMinute(0).withSecond(0).withNano(0);

        //AnimalId ve DoctorId ye göre nesneler üretiliyor
        Animal animal = this.animalService.get(appointmentSaveRequest.getAnimalId());
        Doctor doctor = this.doctorService.get(appointmentSaveRequest.getDoctorId());

        //Doktorun müsait günlerini liste içerisine atıyor
        List<Doctor> doctorList =  this.doctorService.findByIdAndAvailableDateDate(appointmentSaveRequest.getDoctorId(), LocalDate.from(dateTime));
        //Oluşturulan randevular içerisinde çakışan randevuları liste içerisine atıyor.
        List<Appointment> appointmentByDate = this.appointmentService.findByDateTime(dateTime);

        //DoctorId ve AnimalId ler aynı olduğu için update işlemi yapmasın diye null değeri verilir.
        appointmentSaveRequest.setAnimalId(null);
        appointmentSaveRequest.setDateTime(null);
        appointmentSaveRequest.setDoctorId(null);

        //restApi ile setleme işlemi yapılır.
        Appointment saveAppointment = this.modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);
        saveAppointment.setAnimal(animal);
        saveAppointment.setDoctor(doctor);
        saveAppointment.setDateTime(dateTime);

        //Liste içerisine aldığımız değerlerden çakışan varsa bu hata mesajı fırlatır yoksa veritabanına kaydetme işlemi yapar.
        if (doctorList.isEmpty()){
            //return ResultHelper.error("Doktor bu tarihte müsait değildir.");
            throw new RuntimeException("Doktor bu tarihte müsait değildir.");
        } else if (!appointmentByDate.isEmpty()) {
            throw new RuntimeException("Doktorun bu saatte randevusu bulunmaktadır.");
        } else {
            this.appointmentService.save(saveAppointment);
        }
        return ResultHelper.created(this.modelMapperService.forResponse().map(saveAppointment, AppointmentResponse.class));
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
    @GetMapping("/filter/{doctorId}-{findByDate}")
    public List<Appointment> getDoctorNameAndDate(
            @PathVariable("doctorId") int id,
            @RequestParam(name = "entryDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryDate,
            @RequestParam(name = "exitDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime exitDate
    ){
        return this.appointmentService.findByDoctorIdAndDateTimeBetween(id,entryDate,exitDate);
    }

}
