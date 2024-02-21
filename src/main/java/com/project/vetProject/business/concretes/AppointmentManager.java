package com.project.vetProject.business.concretes;

import com.project.vetProject.business.abstracts.IAnimalService;
import com.project.vetProject.business.abstracts.IAppointmentService;
import com.project.vetProject.business.abstracts.IDoctorService;
import com.project.vetProject.core.config.modelMapper.IModelMapperService;
import com.project.vetProject.core.exception.NotFoundException;
import com.project.vetProject.core.result.ResultData;
import com.project.vetProject.core.utilies.Msg;
import com.project.vetProject.core.utilies.ResultHelper;
import com.project.vetProject.dao.AppointmentRepo;
import com.project.vetProject.dto.request.appointment.AppointmentSaveRequest;
import com.project.vetProject.dto.response.appointment.AppointmentResponse;
import com.project.vetProject.entity.Animal;
import com.project.vetProject.entity.Appointment;
import com.project.vetProject.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final IAnimalService animalService;
    private final IDoctorService doctorService;
    private final IModelMapperService modelMapperService;


    public AppointmentManager(
            AppointmentRepo appointmentRepo,
            IAnimalService animalService,
            IDoctorService doctorService,
            IModelMapperService modelMapperService) {
        this.appointmentRepo = appointmentRepo;
        this.animalService = animalService;
        this.doctorService = doctorService;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest) {
        //LocalDateTime dakikalar 00 değilse hata mesajı veriyor.
        LocalDateTime dateTime = appointmentSaveRequest.getDateTime();
        System.out.println(dateTime.getMinute());
        if (dateTime.getMinute() != 0){
            return ResultHelper.error("Lütfen dakika bilgisini '00' giriniz.");
        }

        //AnimalId ve DoctorId ye göre nesneler üretiliyor
        Animal animal = this.animalService.get(appointmentSaveRequest.getAnimalId());
        Doctor doctor = this.doctorService.get(appointmentSaveRequest.getDoctorId());

        //Doktorun müsait günlerini liste içerisine atıyor
        List<Doctor> doctorList =  this.doctorService.findByIdAndAvailableDateDate(appointmentSaveRequest.getDoctorId(), LocalDate.from(dateTime));

        //Oluşturulan randevular içerisinde çakışan randevuları liste içerisine atıyor.
        List<Appointment> appointmentByDate = this.findByDateTime(dateTime);

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
            return ResultHelper.error("Doktor bu tarihte müsait değildir.");

        } else if (!appointmentByDate.isEmpty()) {
            return ResultHelper.error("Doktorun bu saatte randevusu bulunmaktadır.");

        } else {
            Appointment appointment = this.appointmentRepo.save(saveAppointment);
            return ResultHelper.created(this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));
        }
    }

    @Override
    public Appointment get(int id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.appointmentRepo.findAll(pageable);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.get(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public boolean delete(int id) {
        Appointment appointment = this.get(id);
        this.appointmentRepo.delete(appointment);
        return true;
    }

    @Override
    public List<Appointment> findByDateTime(LocalDateTime localDateTime) {
        return this.appointmentRepo.findByDateTime(localDateTime);
    }

    @Override
    public List<Appointment> findByDoctorIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate) {
        return this.appointmentRepo.findByDoctorIdAndDateTimeBetween(id, entryDate, exitDate);
    }

    @Override
    public List<Appointment> findByAnimalIdAndDateTimeBetween(int id, LocalDateTime entryDate, LocalDateTime exitDate) {
        return this.appointmentRepo.findByAnimalIdAndDateTimeBetween(id, entryDate, exitDate);
    }
}
