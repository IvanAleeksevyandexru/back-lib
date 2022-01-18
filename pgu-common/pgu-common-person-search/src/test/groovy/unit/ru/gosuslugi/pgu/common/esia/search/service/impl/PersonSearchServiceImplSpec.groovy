package unit.ru.gosuslugi.pgu.common.esia.search.service.impl

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException
import ru.gosuslugi.pgu.common.esia.search.dto.PersonWithAge

import org.springframework.http.HttpMethod
import ru.gosuslugi.pgu.common.esia.search.service.UddiService
import ru.gosuslugi.pgu.common.esia.search.service.impl.PersonSearchServiceImpl
import spock.lang.Specification

class PersonSearchServiceImplSpec extends Specification {

    PersonSearchServiceImpl service
    RestTemplate restTemplateMock

    String snils = 'snils'
    String series = 'series'
    String number = 'number'
    PersonWithAge person = new PersonWithAge(oid: '4815162342', lastName: 'Васин', trusted: true)
    PersonWithAge notTrustedPerson1 = new PersonWithAge(oid:'2456752222', lastName: 'Широков', trusted: false)
    PersonWithAge notTrustedPerson2 = new PersonWithAge(oid:'8301456333', lastName: 'Капустин', trusted: false)

    String systemTokenHost = "systemTokenHost"

    def setup() {
        restTemplateMock = Mock(RestTemplate)
        restTemplateMock.getForObject(_ as String, String.class) >> new String('token')

        service = new PersonSearchServiceImpl(restTemplateMock, Stub(UddiService))
        service.systemTokenHost = systemTokenHost
    }

    def "Can get person by snils"() {
        given:
        List<PersonWithAge> result

        when:
        restTemplateMock.exchange('pso/srch/by-snils?snils={snils}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([person], HttpStatus.OK)
        result = service.bySnils(snils)

        then:
        result.size() == 1
        result[0].oid == '4815162342'
        result[0].lastName == 'Васин'
    }

    def "Can get person by passport"() {
        given:
        List<PersonWithAge> result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([person], HttpStatus.OK)
        result = service.byPassport(series, number)

        then:
        result.size() == 1
        result[0].oid == '4815162342'
        result[0].lastName == 'Васин'
    }

    def 'Can get null if service throw exception'() {
        given:
        List<PersonWithAge> result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                {throw new EntityNotFoundException('')}
        result = service.byPassport(series, number)

        then:
        result == null
    }

    def "Can get no person by passport"() {
        given:
        List<PersonWithAge> result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([], HttpStatus.OK)
        result = service.byPassport(series, number)

        then:
        result.size() == 0
    }

    def "Can get no person by snils"() {
        given:
        List<PersonWithAge> result

        when:
        restTemplateMock.exchange('pso/srch/by-snils?snils={snils}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([], HttpStatus.OK)
        result = service.bySnils(snils)

        then:
        result.size() == 0
    }

    def "get trusted person with searchOneTrusted by snils"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-snils?snils={snils}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([notTrustedPerson1, notTrustedPerson2, person], HttpStatus.OK)
        result = service.searchOneTrusted(snils)

        then:
        result != null
        result.oid == '4815162342'
        result.lastName == 'Васин'
    }

    def "get no trusted person with searchOneTrusted by snils"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-snils?snils={snils}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([notTrustedPerson1, notTrustedPerson2], HttpStatus.OK)
        result = service.searchOneTrusted(snils)

        then:
        result == null
    }

    def "Can get no person with searchOneTrusted by snils"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-snils?snils={snils}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([], HttpStatus.OK)
        result = service.searchOneTrusted(snils)

        then:
        result == null
    }

    def "get trusted person with searchOneTrusted by passport"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([notTrustedPerson1, notTrustedPerson2, person], HttpStatus.OK)
        result = service.searchOneTrusted(series, number)

        then:
        result != null
        result.oid == '4815162342'
        result.lastName == 'Васин'
    }

    def "get no trusted person with searchOneTrusted by passport"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([notTrustedPerson1, notTrustedPerson2], HttpStatus.OK)
        result = service.searchOneTrusted(series, number)

        then:
        result == null
    }

    def "Can get no person with searchOneTrusted by passport"() {
        given:
        PersonWithAge result

        when:
        restTemplateMock.exchange('pso/srch/by-passport?series={series}&number={number}', HttpMethod.GET, _ as HttpEntity, _ as ParameterizedTypeReference, _ as Map<String, String>) >>
                new ResponseEntity([], HttpStatus.OK)
        result = service.searchOneTrusted(series, number)

        then:
        result == null
    }
}