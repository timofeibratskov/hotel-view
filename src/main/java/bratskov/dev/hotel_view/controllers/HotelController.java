package bratskov.dev.hotel_view.controllers;

import bratskov.dev.hotel_view.entities.HotelEntity;
import bratskov.dev.hotel_view.repositories.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelRepository hotelRepository;

    @GetMapping("/hi")
    public String getHi(){
        return "hi!";
    }

    @GetMapping("/hotels/all")
    public List<HotelEntity> getAll(){
        return hotelRepository.findAll();
    }
    @PostMapping("/hotels")
    public void getAll(@RequestBody HotelEntity entity){
        hotelRepository.save(entity);
    }

}
