package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryPost;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private  CountryRepository countryRepository;

        @Autowired
        private RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                List<Country> countries = response.stream().map(this::mapToCountry).collect(Collectors.toList());
                return countries;
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                Map<String, String> languages = (Map<String, String>) countryData.get("languages");

                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .code((String) countryData.get("cca3"))
                        .region((String) countryData.get("region"))
                        .borders((List<String>) countryData.get("borders"))
                        .languages(languages)
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }

        public List<CountryDTO> getCountriesPorNombreYCodigo(String code, String name) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                List<Country> countries = response.stream()
                        .map(this::mapToCountry)
                        .collect(Collectors.toList());

                return countries.stream()
                        .filter(country -> (code == null || country.getCode().equalsIgnoreCase(code)) &&
                                (name == null || country.getName().equalsIgnoreCase(name)))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());

        }

        public List<CountryDTO> getCountriesPorContinente(String continent) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                List<Country> countries = response.stream()
                        .map(this::mapToCountry)
                        .collect(Collectors.toList());

                return countries.stream()
                        .filter(country -> country.getRegion().equalsIgnoreCase(continent))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountriesPorIdioma(String idioma) {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                List<Country> countries = response.stream()
                        .map(this::mapToCountry)
                        .collect(Collectors.toList());

                return countries.stream()
                        .filter(country -> country.getLanguages() != null &&
                                country.getLanguages().values().stream()
                                        .anyMatch(lang -> lang.equalsIgnoreCase(idioma)))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public CountryDTO getCountrieMasFrontera() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

                List<Country> countries = response.stream()
                        .map(this::mapToCountry)
                        .collect(Collectors.toList());

                Country countryMost = countries.stream()
                        .max(Comparator.comparingInt(country -> country.getBorders() != null ? country.getBorders().size() : 0))
                        .orElse(null);

                return countryMost != null ? mapToDTO(countryMost) : null;
        }


        public List<CountryDTO> postCountries(Long amountOfCountryToSave) {
                List<Country> allCountries = getAllCountries();
                Collections.shuffle(allCountries);

                List<CountryPost> selectedCountries = allCountries.stream()
                        .limit(amountOfCountryToSave)
                        .map(country -> new CountryPost(null, country.getCode(), country.getName()))
                        .collect(Collectors.toList());

                List<CountryPost> savedCountries = countryRepository.saveAll(selectedCountries);

                return savedCountries.stream()
                        .map(savedCountry -> new CountryDTO(savedCountry.getCode(), savedCountry.getName()))
                        .collect(Collectors.toList());
        }




}