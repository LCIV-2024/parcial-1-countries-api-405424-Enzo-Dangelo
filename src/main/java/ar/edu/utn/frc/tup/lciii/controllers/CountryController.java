package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDTO>> obtenerCountries(@RequestParam(required = false) String code, @RequestParam(required = false) String name) {
        List<CountryDTO> countries = countryService.getCountriesPorNombreYCodigo(code, name);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{continent}/continent")
    public ResponseEntity<List<CountryDTO>> obtenerCountriesPorContinente(@PathVariable String continent) {
        List<CountryDTO> countries = countryService.getCountriesPorContinente(continent);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{language}/language")
    public ResponseEntity<List<CountryDTO>> obtenerCountriesPorIdioma(@PathVariable String language) {
        List<CountryDTO> countries = countryService.getCountriesPorIdioma(language);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/most-borders")
    public ResponseEntity<CountryDTO> obtenerCountryConMasFronteras() {
        CountryDTO country = countryService.getCountrieMasFrontera();
        return ResponseEntity.ok(country);
    }

    @PostMapping
    public ResponseEntity<List<CountryDTO>> postCountries(@RequestBody CountryRequestDTO request) {
        List<CountryDTO> savedCountries = countryService.postCountries(request.getAmountOfCountryToSave());
        return ResponseEntity.ok(savedCountries);
    }

}