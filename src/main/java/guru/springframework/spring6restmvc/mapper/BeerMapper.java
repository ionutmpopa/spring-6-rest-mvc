package guru.springframework.spring6restmvc.mapper;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.domain.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);

}
