package com.richard.earthquake.processor.domain.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.richard.earthquake.processor.data.dto.EarthquakeDto;
import com.richard.earthquake.processor.data.model.Earthquake;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ObjectMapper {
    private static String countryJson =
            "{\"Canada\": \"ca\",\"Congo (Republic of the)\": \"cg\",\"Turkmenistan\": \"tm\",\"Lithuania\": \"lt\",\"Sao Tome and Principe\": \"st\",\"Cambodia\": \"kh\",\"Ethiopia\": \"et\",\"Aruba\": \"aw\",\"Swaziland\": \"sz\",\"Argentina\": \"ar\",\"Bolivia\": \"bo\",\"Cameroon\": \"cm\",\"Burkina Faso\": \"bf\",\"Ghana\": \"gh\",\"Korea (North)\": \"kp\",\"Saudi Arabia\": \"sa\",\"Slovenia\": \"si\",\"Guatemala\": \"gt\",\"Bosnia and Herzegovina\": \"ba\",\"Guinea\": \"gn\",\"Germany\": \"de\",\"Dominica\": \"dm\",\"Australia\": \"au\",\"Liberia\": \"lr\",\"Maldives\": \"mv\",\"Paraguay\": \"py\",\"Pakistan\": \"pk\",\"Oman\": \"om\",\"Tanzania\": \"tz\",\"Cabo Verde\": \"cv\",\"Gabon\": \"ga\",\"Monaco\": \"mc\",\"Bahamas\": \"bs\",\"New Zealand\": \"nz\",\"Yemen\": \"ye\",\"Jamaica\": \"jm\",\"Albania\": \"al\",\"Samoa\": \"ws\",\"Macau\": \"mo\",\"United Arab Emirates\": \"ae\",\"Kosovo\": \"xk\",\"India\": \"in\",\"Azerbaijan\": \"az\",\"Madagascar\": \"mg\",\"Lesotho\": \"ls\",\"Saint Vincent and the Grenadines\": \"vc\",\"Kenya\": \"ke\",\"Tajikistan\": \"tj\",\"Turkey\": \"tr\",\"Afghanistan\": \"af\",\"Bangladesh\": \"bd\",\"Eritrea\": \"er\",\"Congo (Democratic Republic of the)\": \"cd\",\"Saint Lucia\": \"lc\",\"San Marino\": \"sm\",\"Mongolia\": \"mn\",\"France\": \"fr\",\"Rwanda\": \"rw\",\"Slovakia\": \"sk\",\"Somalia\": \"so\",\"Peru\": \"pe\",\"Laos\": \"la\",\"Nauru\": \"nr\",\"Norway\": \"NO\",\"Cote d'Ivoire\": \"ci\",\"Benin\": \"bj\",\"Cuba\": \"cu\",\"Korea (South)\": \"kr\",\"Montenegro\": \"me\",\"Saint Kitts and Nevis\": \"kn\",\"Togo\": \"tg\",\"China\": \"cn\",\"Armenia\": \"am\",\"Antigua and Barbuda\": \"ag\",\"Dominican Republic\": \"do\",\"Ukraine\": \"ua\",\"Bahrain\": \"bh\",\"Tonga\": \"to\",\"Indonesia\": \"id\",\"Libya\": \"ly\",\"Finland\": \"fi\",\"Central African Republic\": \"cf\",\"Mauritius\": \"mu\",\"Liechtenstein\": \"li\",\"Vietnam\": \"vn\",\"Mali\": \"ml\",\"Russia\": \"ru\",\"Bulgaria\": \"bg\",\"United States\": \"us\",\"USA\": \"us\",\"Romania\": \"ro\",\"Angola\": \"ao\",\"Chad\": \"td\",\"South Africa\": \"za\",\"Fiji\": \"fj\",\"Sweden\": \"se\",\"Malaysia\": \"my\",\"Austria\": \"at\",\"Mozambique\": \"mz\",\"Uganda\": \"ug\",\"Japan\": \"jp\",\"Niger\": \"ne\",\"Brazil\": \"br\",\"Kuwait\": \"kw\",\"Panama\": \"pa\",\"Guyana\": \"gy\",\"Costa Rica\": \"cr\",\"Luxembourg\": \"lu\",\"Andorra\": \"ad\",\"Ireland\": \"ie\",\"Italy\": \"it\",\"Nigeria\": \"ng\",\"Ecuador\": \"ec\",\"Brunei\": \"bn\",\"Belarus\": \"by\",\"Iran\": \"ir\",\"Algeria\": \"dz\",\"El Salvador\": \"sv\",\"Tuvalu\": \"tv\",\"Czechia\": \"cz\",\"Solomon Islands\": \"sb\",\"Marshall Islands\": \"mh\",\"Chile\": \"cl\",\"Belgium\": \"be\",\"Kiribati\": \"ki\",\"Haiti\": \"ht\",\"Belize\": \"bz\",\"Hong Kong\": \"hk\",\"Sierra Leone\": \"sl\",\"Georgia\": \"ge\",\"Gambia\": \"gm\",\"Philippines\": \"ph\",\"Moldova\": \"md\",\"Morocco\": \"ma\",\"Croatia\": \"hr\",\"Malta\": \"mt\",\"Guinea-Bissau\": \"gw\",\"Thailand\": \"th\",\"Switzerland\": \"ch\",\"Grenada\": \"gd\",\"Seychelles\": \"sc\",\"Portugal\": \"pt\",\"Estonia\": \"ee\",\"Uruguay\": \"uy\",\"Equatorial Guinea\": \"gq\",\"Lebanon\": \"lb\",\"Uzbekistan\": \"uz\",\"Egypt\": \"eg\",\"Djibouti\": \"dj\",\"Timor-Leste\": \"tl\",\"Spain\": \"es\",\"Colombia\": \"co\",\"Burundi\": \"bi\",\"Taiwan\": \"tw\",\"Cyprus\": \"cy\",\"Barbados\": \"bb\",\"Qatar\": \"qa\",\"Palau\": \"pw\",\"Curacao\": \"cw\",\"Bhutan\": \"bt\",\"Sudan\": \"sd\",\"Palestinian Territories\": \"ps\",\"Nepal\": \"np\",\"Micronesia\": \"fm\",\"Netherlands\": \"nl\",\"Suriname\": \"sr\",\"Venezuela\": \"ve\",\"Holy See\": \"va\",\"Israel\": \"il\",\"Iceland\": \"is\",\"Zambia\": \"zm\",\"Senegal\": \"sn\",\"Papua New Guinea\": \"pg\",\"Malawi\": \"mw\",\"Zimbabwe\": \"zw\",\"Jordan\": \"jo\",\"Vanuatu\": \"vu\",\"Denmark\": \"dk\",\"Kazakhstan\": \"kz\",\"Poland\": \"pl\",\"Mauritania\": \"mr\",\"Kyrgyzstan\": \"kg\",\"Iraq\": \"iq\",\"Macedonia\": \"mk\",\"Trinidad and Tobago\": \"tt\",\"Latvia\": \"lv\",\"South Sudan\": \"ss\",\"Hungary\": \"hu\",\"Syria\": \"sy\",\"Sint Maarten\": \"sx\",\"Honduras\": \"hn\",\"Myanmar\": \"mm\",\"Mexico\": \"mx\",\"Tunisia\": \"tn\",\"Nicaragua\": \"ni\",\"Singapore\": \"sg\",\"Serbia\": \"rs\",\"Comoros\": \"km\",\"United Kingdom\": \"gb\",\"England\": \"gb\",\"Greece\": \"gr\",\"Sri Lanka\": \"lk\",\"Namibia\": \"na\",\"Botswana\": \"bw\"}";
    private static Map<String, String> countryCodes;

    public static Earthquake map(EarthquakeDto earthquakeDto) {

        Earthquake earthquake = new Earthquake();
        earthquake.setType(earthquakeDto.getType());
        earthquake.setId(earthquakeDto.getId());
        earthquake.setProperties(earthquakeDto.getProperties());
        earthquake.setDepth(earthquakeDto.getGeometry().getCoordinates().get(2));
        earthquake.setGeometry(new GeometryFactory().createPoint(new Coordinate(earthquakeDto.getGeometry().getCoordinates().get(0), earthquakeDto.getGeometry().getCoordinates().get(1))));
        earthquake.setCountry(earthquakeDto.getProperties().getPlace().substring(earthquake.getProperties().getPlace().lastIndexOf(" ") + 1));
        earthquake.setCountryCode(earthquake.getCountry() == null ? null : getCountryCode(earthquake.getCountry()));
        earthquake.setModifiedTime(earthquakeDto.getProperties().getUpdated() == null ? earthquakeDto.getProperties().getTime() : earthquakeDto.getProperties().getUpdated());
        earthquake.setMag(earthquakeDto.getProperties().getMag());
        earthquake.setTime(earthquakeDto.getProperties().getTime());
        return earthquake;
    }

    public static Mono<Earthquake> mapMono(EarthquakeDto earthquakeDto) {
        return Mono.just(earthquakeDto)
                .map(ObjectMapper::map)
                .onErrorResume(throwable -> Mono.empty());

    }

    public static String getCountryCode(String country) {
        String countryCode = null;
        if (countryCodes == null)
            generateCountryCodes();

        for (Map.Entry<String, String> entry : countryCodes.entrySet()) {
            if (entry.getKey().toLowerCase().contains(country.toLowerCase())) {
                countryCode = entry.getValue().toUpperCase();
                break;
            }
        }

        if (countryCode == null) {
            if (country.length() == 2)
                countryCode = country;
        }

        return countryCode;
    }

    private static void generateCountryCodes() {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        try {
            countryCodes = objectMapper.readValue(countryJson, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
