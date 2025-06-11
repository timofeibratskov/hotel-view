package bratskov.dev.hotel_view.services;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Expression;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.params.ParameterizedTest;
import bratskov.dev.hotel_view.mappers.HotelMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.params.provider.EnumSource;
import bratskov.dev.hotel_view.entities.HotelEntity;
import bratskov.dev.hotel_view.enums.HistogramParam;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;
import org.springframework.data.jpa.domain.Specification;
import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.dtos.HotelSearchCriteriaDTO;
import bratskov.dev.hotel_view.repositories.HotelRepository;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;
import bratskov.dev.hotel_view.exceptions.HotelNotFoundException;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HotelServiceTest {

    @InjectMocks
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Tuple> criteriaQuery;

    @Mock
    private Root<HotelEntity> root;

    @Mock
    private jakarta.persistence.TypedQuery<Tuple> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createTupleQuery()).thenReturn(criteriaQuery);
        when(criteriaQuery.from(HotelEntity.class)).thenReturn(root);

        when(criteriaQuery.multiselect(any(Selection[].class))).thenReturn(criteriaQuery);
        when(criteriaQuery.groupBy(any(Expression.class))).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(Predicate.class))).thenReturn(criteriaQuery);

        when(criteriaBuilder.count(any(Expression.class))).thenReturn(mock(Expression.class));
        when(root.join("amenities")).thenReturn(mock(Join.class));
        when(root.get("brand")).thenReturn(mock(Path.class));
        when(root.get("address")).thenReturn(mock(Path.class));
        when(root.get("address").get("city")).thenReturn(mock(Path.class));
        when(root.get("address").get("country")).thenReturn(mock(Path.class));
        when(criteriaBuilder.isNotNull(any())).thenReturn(mock(Predicate.class));
    }

    @Test
    @DisplayName("Должен создать отель и вернуть краткий DTO")
    void shouldCreateHotelAndReturnShortDto() {
        CreateHotelRequest request = CreateHotelRequest.builder()
                .name("DoubleTree by Hilton Minsk")
                .description("Luxurious hotel with city views")
                .brand("Hilton")
                .address(Address.builder()
                        .street("Pobediteley Avenue")
                        .houseNumber(9)
                        .city("Minsk")
                        .country("Belarus")
                        .postCode("220004")
                        .build())
                .contacts(Contacts.builder()
                        .phone("+375 17 309-80-00")
                        .email("info@hotel.com")
                        .build())
                .arrivalTime(ArrivalTime.builder()
                        .checkIn("14:00")
                        .checkOut("12:00")
                        .build())
                .build();

        HotelEntity savedEntity = HotelEntity.builder()
                .id(1L)
                .name(request.name())
                .description(request.description())
                .brand(request.brand())
                .address(request.address())
                .contacts(request.contacts())
                .arrivalTime(request.arrivalTime())
                .build();

        HotelShortDto expectedDto = HotelShortDto.builder()
                .id(1L)
                .name(request.name())
                .description(request.description())
                .address("9 Pobediteley Avenue, Minsk, 220004, Belarus")
                .phone("+375 17 309-80-00")
                .build();

        when(hotelMapper.toEntity(request)).thenReturn(savedEntity);
        when(hotelRepository.save(any(HotelEntity.class))).thenReturn(savedEntity);
        when(hotelMapper.toShortDto(savedEntity)).thenReturn(expectedDto);

        HotelShortDto result = hotelService.createHotel(request);

        assertEquals(expectedDto.id(), result.id(), "ID отеля должен совпадать");
        assertEquals(expectedDto.name(), result.name(), "Имя отеля должно совпадать");
        assertEquals(expectedDto.description(), result.description(), "Описание должно совпадать");
        assertEquals(expectedDto.address(), result.address(), "Адрес должен совпадать");
        assertEquals(expectedDto.phone(), result.phone(), "Телефон должен совпадать");
        verify(hotelRepository, times(1)).save(any(HotelEntity.class));
        verify(hotelMapper, times(1)).toEntity(request);
        verify(hotelMapper, times(1)).toShortDto(savedEntity);
    }

    @Test
    @DisplayName("Должен добавить удобства к отелю и сохранить")
    void shouldAddAmenitiesToHotelAndSave() {
        Long existentId = 1L;
        List<String> amenitiesToAdd = List.of("Free WiFi", "Pool");
        Set<String> initialAmenities = new HashSet<>();
        HotelEntity entity = HotelEntity.builder()
                .id(existentId)
                .name("DoubleTree by Hilton Minsk")
                .amenities(initialAmenities)
                .build();

        when(hotelRepository.findById(existentId)).thenReturn(Optional.of(entity));
        when(hotelRepository.save(any(HotelEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        hotelService.addAmenities(existentId, amenitiesToAdd);

        assertEquals(2, entity.getAmenities().size(), "Должно быть добавлено 2 удобства");
        assertTrue(entity.getAmenities().containsAll(amenitiesToAdd), "Все удобства должны быть добавлены");
        verify(hotelRepository, times(1)).findById(existentId);
        verify(hotelRepository, times(1)).save(any(HotelEntity.class));
    }

    @Test
    @DisplayName("Должен вернуть полный DTO отеля, если отель найден")
    void shouldReturnHotelFullDtoWhenHotelFoundForGetHotelById() {
        Long existentId = 1L;
        HotelEntity entity = HotelEntity.builder()
                .id(existentId)
                .name("DoubleTree by Hilton Minsk")
                .build();
        HotelFullDto expectedDto = HotelFullDto.builder()
                .id(existentId)
                .name("DoubleTree by Hilton Minsk")
                .build();

        when(hotelRepository.findById(existentId)).thenReturn(Optional.of(entity));
        when(hotelMapper.toFullDto(entity)).thenReturn(expectedDto);

        HotelFullDto result = hotelService.getHotelById(existentId);

        assertEquals(expectedDto.id(), result.id(), "ID отеля должен совпадать");
        assertEquals(expectedDto.name(), result.name(), "Имя отеля должно совпадать");
        verify(hotelRepository, times(1)).findById(existentId);
        verify(hotelMapper, times(1)).toFullDto(entity);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если отель не найден для getHotelById")
    void shouldThrowExceptionWhenHotelNotFoundForGetHotelById() {
        Long nonExistentId = 999L;
        when(hotelRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        HotelNotFoundException exception = assertThrows(HotelNotFoundException.class,
                () -> hotelService.getHotelById(nonExistentId),
                "Должно выбросить исключение, если отель не найден");
        assertEquals("Hotel not found with id: " + nonExistentId, exception.getMessage(),
                "Сообщение об ошибке должно совпадать");
        verify(hotelRepository, times(1)).findById(nonExistentId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если отель не найден для addAmenities")
    void shouldThrowExceptionWhenHotelNotFoundForAddAmenities() {
        Long nonExistentId = 999L;
        List<String> amenities = List.of("Free WiFi", "Pool");
        when(hotelRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        HotelNotFoundException exception = assertThrows(HotelNotFoundException.class,
                () -> hotelService.addAmenities(nonExistentId, amenities),
                "Должно выбросить исключение, если отель не найден");
        assertEquals("Hotel not found with id: " + nonExistentId, exception.getMessage(),
                "Сообщение об ошибке должно совпадать");
        verify(hotelRepository, times(1)).findById(nonExistentId);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("Должен вернуть все отели, если они есть")
    void shouldReturnAllHotelsWhenAvailable() {
        HotelEntity entity1 = HotelEntity.builder().id(1L).name("Hotel A").build();
        HotelEntity entity2 = HotelEntity.builder().id(2L).name("Hotel B").build();
        List<HotelEntity> entities = List.of(entity1, entity2);
        HotelShortDto dto1 = HotelShortDto.builder().id(1L).name("Hotel A").build();
        HotelShortDto dto2 = HotelShortDto.builder().id(2L).name("Hotel B").build();
        List<HotelShortDto> expectedDtos = List.of(dto1, dto2);

        when(hotelRepository.findAll()).thenReturn(entities);
        when(hotelMapper.toShortDto(entity1)).thenReturn(dto1);
        when(hotelMapper.toShortDto(entity2)).thenReturn(dto2);

        List<HotelShortDto> result = hotelService.getAllHotels();

        assertEquals(2, result.size(), "Должно вернуть 2 отеля");
        assertEquals(expectedDtos.get(0).id(), result.get(0).id(), "ID первого отеля должен совпадать");
        assertEquals(expectedDtos.get(1).id(), result.get(1).id(), "ID второго отеля должен совпадать");
        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(1)).toShortDto(entity1);
        verify(hotelMapper, times(1)).toShortDto(entity2);
    }

    @Test
    @DisplayName("Должен вернуть пустой список, если отелей нет")
    void shouldReturnEmptyListWhenNoHotels() {
        when(hotelRepository.findAll()).thenReturn(Collections.emptyList());

        List<HotelShortDto> result = hotelService.getAllHotels();

        assertEquals(0, result.size(), "Список должен быть пустым");
        verify(hotelRepository, times(1)).findAll();
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("Должен искать отели по критериям")
    void shouldSearchHotelsWithCriteria() {
        HotelSearchCriteriaDTO criteria = HotelSearchCriteriaDTO.builder()
                .city("Minsk")
                .build();
        HotelEntity entity = HotelEntity.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .address(Address.builder().city("Minsk").build())
                .build();
        HotelShortDto dto = HotelShortDto.builder()
                .id(1L)
                .name("DoubleTree by Hilton Minsk")
                .build();
        List<HotelEntity> entities = List.of(entity);

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(entities);
        when(hotelMapper.toShortDto(entity)).thenReturn(dto);

        List<HotelShortDto> result = hotelService.searchHotels(criteria);

        assertEquals(1, result.size(), "Должен вернуть 1 отель");
        assertEquals(dto.id(), result.get(0).id(), "ID отеля должен совпадать");
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
        verify(hotelMapper, times(1)).toShortDto(entity);
    }

    @Test
    @DisplayName("Должен вернуть пустой список, если нет результатов поиска")
    void shouldReturnEmptyListWhenNoSearchResults() {
        HotelSearchCriteriaDTO criteria = HotelSearchCriteriaDTO.builder()
                .city("NonExistentCity")
                .build();
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<HotelShortDto> result = hotelService.searchHotels(criteria);

        assertEquals(0, result.size(), "Список должен быть пустым");
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
        verifyNoInteractions(hotelMapper);
    }

    @Test
    @DisplayName("Должен выбросить исключение для неверного параметра гистограммы")
    void shouldThrowExceptionForInvalidHistogramParam() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.getHistogram(null),
                "Должно выбросить исключение для null параметра");
        assertEquals("Unsupported histogram param: null", exception.getMessage(),
                "Сообщение об ошибке должно совпадать");
        verifyNoInteractions(hotelRepository, entityManager);
    }

    @ParameterizedTest
    @EnumSource(HistogramParam.class)
    @DisplayName("Должен вернуть гистограмму для всех параметров")
    void shouldReturnHistogram(HistogramParam param) {
        Tuple tuple1 = mock(Tuple.class);
        when(tuple1.get(0, String.class)).thenReturn("Hilton");
        when(tuple1.get(1, Long.class)).thenReturn(2L);

        Tuple tuple2 = mock(Tuple.class);
        when(tuple2.get(0, String.class)).thenReturn("Marriott");
        when(tuple2.get(1, Long.class)).thenReturn(1L);

        List<Tuple> mockTuples = List.of(tuple1, tuple2);

        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockTuples);

        Map<String, Long> result = hotelService.getHistogram(param);

        assertEquals(2, result.size(), "Гистограмма должна содержать 2 записи");
        assertEquals(2L, result.get("Hilton"), "Количество для Hilton должно быть 2");
        assertEquals(1L, result.get("Marriott"), "Количество для Marriott должно быть 1");
        verify(entityManager, times(1)).createQuery(any(CriteriaQuery.class));
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    @DisplayName("Должен вернуть пустую гистограмму, если нет данных")
    void shouldReturnEmptyHistogramWhenNoData() {
        when(entityManager.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        Map<String, Long> result = hotelService.getHistogram(HistogramParam.BRAND);

        assertTrue(result.isEmpty(), "Гистограмма должна быть пустой");
        verify(entityManager, times(1)).createQuery(any(CriteriaQuery.class));
        verify(typedQuery, times(1)).getResultList();
    }
}