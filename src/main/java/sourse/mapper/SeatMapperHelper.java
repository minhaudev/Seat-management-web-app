package sourse.mapper;

import org.mapstruct.Named;
import sourse.enums.EnumType;

public class SeatMapperHelper {

    @Named("mapStatus")
    public static EnumType.SeatStatus mapStatus(EnumType.SeatStatus value) {
        return value;
    }

    @Named("mapTypeSeat")
    public static EnumType.TypeSeat mapTypeSeat(EnumType.TypeSeat value) {
        return value;
    }
}
