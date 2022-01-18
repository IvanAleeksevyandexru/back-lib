package unit.ru.gosuslugi.pgu.common.esia.search.service.impl

import ru.atc.carcass.security.rest.model.person.Person
import ru.atc.carcass.security.rest.model.person.PersonDoc
import ru.gosuslugi.pgu.common.esia.search.dto.UserPersonalData
import spock.lang.Specification

class UserPersonalDataSpec  extends Specification{

    UserPersonalData userPersonalData

    def setup(){
        userPersonalData = new UserPersonalData()
        userPersonalData.person >> new Person(
                firstName: 'ivan',
                middleName: 'ivanovich',
                lastName: 'petrov',
        )
    }

    def 'Can get oms series and number'() {
        given:
        def omsNumber
        def omsSeries
        def docList = new ArrayList<>()

        docList.add(new PersonDoc(type: 'MDCL_PLCY', vrfStu: 'VERIFIED', series: series, number: number,))
        userPersonalData.setDocs(docList)

        when:
        omsSeries = userPersonalData.getOmsDocument().get().getSeries()
        omsNumber = userPersonalData.getOmsDocument().get().getNumber()

        then:
        omsSeries == expectedOmsSeries
        omsNumber == expectedOmsNumber

        where:
        series                  ||number                         || expectedOmsSeries || expectedOmsNumber
//       формат "ХХХХХХХХХХХХХХХХ"
        null                    || '1111111111111111'            ||  ""               || "1111111111111111"
//       формат "ХХХХ ХХХХ ХХХХ ХХХХ"
        null                    ||  '1111 1111 1111 1111'        ||  ""               || "1111111111111111"
        null                    ||   '1111-1111-1111-1111'       ||  ""               || "1111111111111111"
        null                    || '1111  1111   1111  1111'     ||  ""               || "1111111111111111"
//       формат "ХХХХХХ*ХХХХХХХХХХ"
        null                    ||'111111 1111111111'            ||  "111111"         || "1111111111"
        null                    ||'111111  1111111111'           ||  "111111"         || "1111111111"
        null                    ||'111111-1111111111'            ||  "111111"         || "1111111111"
        null                    ||'111111 - 1111111111'          ||  "111111"         || "1111111111"
//        если есть "№" ориентируемся на него
        null                    ||'111111 №1111111111'           ||  "111111"         || "1111111111"
        null                    ||'111111№1111111111'            ||  "111111"         || "1111111111"
        null                    ||'1oxj 11 №1111111111'          ||  "1oxj11"         || "1111111111"
        null                    ||'1oxj11№1111111111'            ||  "1oxj11"         || "1111111111"
        null                    ||'111111 - №1111111111'         ||  "111111"         || "1111111111"
        null                    ||'111 111-№111 11 11111'        ||  "111111"         || "1111111111"
//        в случае несоответствия формам все в номер, а серия остается пустой
        null                    ||'111dsa1 - №fsfsa111'          ||   ""              || "111dsa1 - №fsfsa111"
        null                    ||'какой то номер'               ||   ""              || "какой то номер"
//        если вдруг серия будет заполнена со стороны ЕСИЯ
        '222222'                ||'3333333333'                   ||   "222222"        || "3333333333"
        'серия'                 ||'номер'                        ||   "серия"         || "номер"
    }
}
