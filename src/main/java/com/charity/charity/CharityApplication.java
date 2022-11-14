package com.charity.charity;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class}) //remove brackets for enable default security config
public class CharityApplication {

	public static void main(String[] args) throws NullPointerException {
		SpringApplication.run(CharityApplication.class, args);


		workMethod();
	}



	public static void workMethod(){
		//взять исходную строку
		//удалить пробелы
		//затем через запятые/разделитель записать все данные в массив
		//затем перебирая массив по эелементам, к каждому элементу добавить чт-то спереди что-то сзади
		//таким образом я получу готовый список для того, чтобы енго вставить в метод для создания массива,
		//а также для создания перевода -
		//для перевода нужно будет сделать еще что-то - сделать еще один массив с переводом + и сратстить с другой строкой

		String allCountrys = "Abkhazia\t,\n" +
				"Australia\t,\n" +
				"Austria\t,\n" +
				"Azerbaijan\t,\n" +
				"Albania\t,\n" +
				"Algeria\t,\n" +
				"Am. Virgin Islands\t,\n" +
				"American Samoa\t,\n" +
				"Angola\t,\n" +
				"Andorra\t,\n" +
				"Antigua and Barbuda\t,\n" +
				"Argentina\t,\n" +
				"Armenia\t,\n" +
				"Aruba\t,\n" +
				"Afghanistan\t,\n" +
				"Bahamas\t,\n" +
				"Bangladesh\t,\n" +
				"Barbados\t,\n" +
				"Bahrain\t,\n" +
				"Belize\t,\n" +
				"Belarus\t,\n" +
				"Belgium\t,\n" +
				"Benin\t,\n" +
				"Bermuda\t,\n" +
				"Bulgaria\t,\n" +
				"Bolivia\t,\n" +
				"Bonaire\t,\n" +
				"Bosnia and Herzegovina\t,\n" +
				"Botswana\t,\n" +
				"Brazil\t,\n" +
				"Br. Virgin Islands\t,\n" +
				"Brunei\t,\n" +
				"Burkina Faso\t,\n" +
				"Burundi\t,\n" +
				"Butane\t,\n" +
				"Vanuatu\t,\n" +
				"Vatican\t,\n" +
				"Great Britain\t,\n" +
				"Hungary\t,\n" +
				"Venezuela\t,\n" +
				"East Timor\t,\n" +
				"Vietnam\t,\n" +
				"Gabon\t,\n" +
				"Haiti\t,\n" +
				"Guyana\t,\n" +
				"Gambia\t,\n" +
				"Ghana\t,\n" +
				"Guatemala\t,\n" +
				"Guinea\t,\n" +
				"Guinea-Bissau\t,\n" +
				"Germany\t,\n" +
				"Honduras\t,\n" +
				"Hong Kong\t,\n" +
				"Grenada\t,\n" +
				"Greece\t,\n" +
				"Georgia\t,\n" +
				"Denmark\t,\n" +
				"D.R. Congo\t,\n" +
				"Jersey\t,\n" +
				"Djibouti\t,\n" +
				"Dominica\t,\n" +
				"Dominican Republic\t,\n" +
				"Egypt\t,\n" +
				"Zambia\t,\n" +
				"Zimbabwe\t,\n" +
				"Israel\t,\n" +
				"India\t,\n" +
				"Indonesia\t,\n" +
				"Jordan\t,\n" +
				"Iraq\t,\n" +
				"Iran\t,\n" +
				"Ireland\t,\n" +
				"Iceland\t,\n" +
				"Spain\t,\n" +
				"Italy\t,\n" +
				"Yemen\t,\n" +
				"Cape Verde\t,\n" +
				"Kazakhstan\t,\n" +
				"Cambodia\t,\n" +
				"Cameroon\t,\n" +
				"Canada\t,\n" +
				"Qatar\t,\n" +
				"Kenya\t,\n" +
				"Cyprus\t,\n" +
				"Kyrgyzstan\t,\n" +
				"Kiribati\t,\n" +
				"China\t,\n" +
				"Colombia\t,\n" +
				"coconut islands\t,\n" +
				"Comoros\t,\n" +
				"Congo\t,\n" +
				"North Korea\t,\n" +
				"Korea\t,\n" +
				"Costa Rica\t,\n" +
				"Ivory Coast\t,\n" +
				"Cuba\t,\n" +
				"Kuwait\t,\n" +
				"Curacao\t,\n" +
				"Laos\t,\n" +
				"Latvia\t,\n" +
				"Lesotho\t,\n" +
				"Liberia\t,\n" +
				"Lebanon\t,\n" +
				"Libya\t,\n" +
				"Lithuania\t,\n" +
				"Liechtenstein\t,\n" +
				"Luxembourg\t,\n" +
				"Mauritius\t,\n" +
				"Mauritania\t,\n" +
				"Madagascar\t,\n" +
				"Macedonia\t,\n" +
				"Malawi\t,\n" +
				"Malaysia\t,\n" +
				"Mali\t,\n" +
				"Malta\t,\n" +
				"Maldives\t,\n" +
				"Morocco\t,\n" +
				"Marshall Islands\t,\n" +
				"Mexico\t,\n" +
				"Mozambique\t,\n" +
				"Moldova (Moldova)\t,\n" +
				"Monaco\t,\n" +
				"Mongolia\t,\n" +
				"Myanmar\t,\n" +
				"Namibia\t,\n" +
				"Nauru\t,\n" +
				"Nepal\t,\n" +
				"Niger\t,\n" +
				"Nigeria\t,\n" +
				"Netherlands\t,\n" +
				"Nicaragua\t,\n" +
				"New Zealand\t,\n" +
				"Norway\t,\n" +
				"UAE\t,\n" +
				"Oman\t,\n" +
				"Cook Islands\t,\n" +
				"Pakistan\t,\n" +
				"Palau\t,\n" +
				"Panama\t,\n" +
				"Papua New Guinea\t,\n" +
				"Paraguay\t,\n" +
				"Peru\t,\n" +
				"Puerto Rico\t,\n" +
				"Poland\t,\n" +
				"Portugal\t,\n" +
				"Russia\t,\n" +
				"Rwanda\t,\n" +
				"Romania\t,\n" +
				"Saba\t,\n" +
				"Salvador\t,\n" +
				"Samoa\t,\n" +
				"San Marino\t,\n" +
				"Sao Tome and Principe\t,\n" +
				"Saudi Arabia\t,\n" +
				"Swaziland\t,\n" +
				"Seychelles\t,\n" +
				"Senegal\t,\n" +
				"Saint Vincent and the Grenadines\t,\n" +
				"Saint Kitts and Nevis\t,\n" +
				"Saint Lucia\t,\n" +
				"Saint Martin\t,\n" +
				"Serbia\t,\n" +
				"Singapore\t,\n" +
				"Sint Eustatius\t,\n" +
				"Syria\t,\n" +
				"Slovakia\t,\n" +
				"Slovenia\t,\n" +
				"USA\t,\n" +
				"Solomon islands\t,\n" +
				"Somalia\t,\n" +
				"Sudan\t,\n" +
				"Suriname\t,\n" +
				"Sierra Leone\t,\n" +
				"Tajikistan\t,\n" +
				"Thailand\t,\n" +
				"Tanzania\t,\n" +
				"Togo\t,\n" +
				"Tokelau\t,\n" +
				"Tonga\t,\n" +
				"Trinidad and Tobago\t,\n" +
				"Tuvalu\t,\n" +
				"Tunisia\t,\n" +
				"Turkmenistan\t,\n" +
				"Turkey\t,\n" +
				"Uganda\t,\n" +
				"Uzbekistan\t,\n" +
				"Ukraine\t,\n" +
				"Wallis and Futuna\t,\n" +
				"Uruguay\t,\n" +
				"Faroe islands\t,\n" +
				"Fed. States of Micronesia\t,\n" +
				"Fiji\t,\n" +
				"Philippines\t,\n" +
				"Finland\t,\n" +
				"Falkland Islands\t,\n" +
				"France\t,\n" +
				"French polynesia\t,\n" +
				"Croatia\t,\n" +
				"CAR\t,\n" +
				"Chad\t,\n" +
				"Montenegro\t,\n" +
				"Czech\t,\n" +
				"Chile\t,\n" +
				"Switzerland\t,\n" +
				"Sweden\t,\n" +
				"Sri Lanka\t,\n" +
				"Ecuador\t,\n" +
				"Equatorial Guinea\t,\n" +
				"Eritrea\t,\n" +
				"Estonia\t,\n" +
				"Ethiopia\t,\n" +
				"South Africa\t,\n" +
				"South Sudan\t,\n" +
				"Jamaica\t,\n" +
				"Japan\t,\n";

			String[] arrayCountrys = allCountrys.split("\\\t,\n");

			for (String i : arrayCountrys) {
//				String ready = "optionsCountry.add(\"" + i + "\");" ;
				String ready = "param.country." + i + "=" + i;


				System.out.println(ready);
			}
	}

}