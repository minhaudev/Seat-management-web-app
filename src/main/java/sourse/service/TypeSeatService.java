package sourse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sourse.dto.request.TypeSeatCreationRequest;
import sourse.dto.response.TypeSeatResponse;
import sourse.entity.Project;
import sourse.entity.TypeSeat;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.TypeSeatMapper;
import sourse.repository.TypeSeatRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypeSeatService {

    private final TypeSeatMapper typeSeatMapper;
    private final TypeSeatRepository typeSeatRepository;
    public void validateTypeSeat(String name) {
         if (typeSeatRepository.existsByName(name)) {
             throw new AppException(ErrorCode.TYPESEAT_EXISTS);
         }
     }
     public TypeSeat findById (String id) {
         return typeSeatRepository.findById(id)
                 .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
     }
    public TypeSeatResponse store(TypeSeatCreationRequest request){
         validateTypeSeat(request.getName());
        TypeSeat typeSeat = typeSeatMapper.toTypeSeat(request);
        typeSeatRepository.save(typeSeat);
        return typeSeatMapper.toTypeSeatResponse(typeSeat);
    }
    public List<TypeSeatResponse> index() {
            return typeSeatMapper.toTypeSeatRepository(typeSeatRepository.findAll());
    }
    public void delete(String id) {
        this.findById(id);
        typeSeatRepository.deleteById(id);
    }
}
