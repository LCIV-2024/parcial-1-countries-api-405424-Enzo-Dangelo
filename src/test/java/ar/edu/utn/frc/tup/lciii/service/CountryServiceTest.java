package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryPost;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CountryServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllCountries() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> country = new HashMap<>();
        country.put("cca3", "USA");
        Map<String, Object> name = new HashMap<>();
        name.put("common", "United States");
        country.put("name", name);
        country.put("population", 331002651);
        country.put("area", 9833517.0);
        mockResponse.add(country);

        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);

        List<Country> result = countryService.getAllCountries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getCode());
        assertEquals("United States", result.get(0).getName());
    }

    @Test
    void getCountriesPorNombreYCodigo() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> country = new HashMap<>();
        country.put("cca3", "USA");

        Map<String, Object> name = new HashMap<>();
        name.put("common", "United States");
        country.put("name", name);

        mockResponse.add(country);

        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);
        List<CountryDTO> result = countryService.getCountriesPorNombreYCodigo("USA", "United States");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USA", result.get(1).getCode());
        assertEquals("United States", result.get(1).getName());
    }

    @Test
    void getCountriesPorContinente() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> country = new HashMap<>();
        country.put("cca3", "USA");
        Map<String, Object> name = new HashMap<>();
        name.put("common", "United States");
        country.put("name", name);
        country.put("region", "North America");
        mockResponse.add(country);

        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);

        List<CountryDTO> result = countryService.getCountriesPorContinente("North America");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getCode());
    }

    @Test
    void getCountriesPorIdioma() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> country = new HashMap<>();
        country.put("cca3", "USA");
        Map<String, Object> name = new HashMap<>();
        name.put("common", "United States");
        country.put("name", name);
        country.put("languages", Collections.singletonMap("eng", "English"));
        mockResponse.add(country);

        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);

        List<CountryDTO> result = countryService.getCountriesPorIdioma("English");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USA", result.get(0).getCode());
    }

    @Test
    void getCountrieMasFrontera() {
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> country = new HashMap<>();
        country.put("cca3", "USA");
        Map<String, Object> name = new HashMap<>();
        name.put("common", "United States");
        country.put("name", name);
        country.put("borders", Arrays.asList("CAN", "MEX"));
        mockResponse.add(country);

        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(mockResponse);

        CountryDTO result = countryService.getCountrieMasFrontera();

        assertNotNull(result);
        assertEquals("USA", result.getCode());
    }

}