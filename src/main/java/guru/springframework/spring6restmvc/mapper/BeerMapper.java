package guru.springframework.spring6restmvc.mapper;

import guru.springframework.spring6restmvc.controller.model.BeerDTO;
import guru.springframework.spring6restmvc.controller.model.PageBeerDTO;
import guru.springframework.spring6restmvc.entities.Beer;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);

    PageBeerDTO pageBeerToPageBeerDTO(Page<Beer> beerPage);

}
