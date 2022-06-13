package com.testwork.test;

import com.testwork.test.clients.ExchangeService;
import com.testwork.test.clients.GifService;
import com.testwork.test.controllers.GifController;
import com.testwork.test.models.ExchangeModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestApplicationTests {

	@Autowired
	private GifController gifController;

	@MockBean
	private ExchangeService exchangeService;

	@MockBean
	private GifService gifService;

	@Test
	public void testRichGif() {
		ExchangeModel testModelYesterday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991959L,
				"USD", null);
		Map<String, BigDecimal> ratesYesterday = new HashMap<>();
		ratesYesterday.put("RUB", new BigDecimal(57.624893).setScale(6, RoundingMode.HALF_EVEN));
		testModelYesterday.setRates(ratesYesterday);


		ExchangeModel testModelToday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991960L,
				"USD", null);
		Map<String, BigDecimal> ratesToday = new HashMap<>();
		ratesToday.put("RUB", new BigDecimal(55.624893).setScale(6, RoundingMode.HALF_EVEN));
		testModelToday.setRates(ratesToday);

		String yesterdayDate = Instant.now().minus(1, ChronoUnit.DAYS)
				.atZone(ZoneOffset.UTC)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
		Mockito.when(exchangeService.getExchangeRates("RUB")).thenReturn(testModelToday);
		Mockito.when(exchangeService.getExchangeRates(yesterdayDate, "RUB")).thenReturn(testModelYesterday);

		String gifSource = "https://media0.giphy.com/media/2w6l4or9WPCSuVBlHj/giphy.gif?cid=0039d4398e9810993a7fa1f686c84c367975b01163804e97&rid=giphy.gif&ct=g";
		Mockito.when(gifService.getGif("rich")).thenReturn(String.format("{ \"data\" : { \"images\" : { \"downsized_large\": { \"url\": \"%s\" } } } }", gifSource));

		Assert.isTrue(String.format("<img style=\"display: block;\n" +
				"  margin-left: auto;\n" +
				"  margin-right: auto;\n" +
				"  width: 50%% ;\" src=\"%s\">", gifSource).equals(gifController.getGif("RUB")), "Error!");

		Mockito.verify(exchangeService).getExchangeRates("RUB");
		Mockito.verify(exchangeService).getExchangeRates(yesterdayDate, "RUB");

		Mockito.verify(gifService).getGif("rich");
	}

	@Test
	public void testBrokeGif() {
		ExchangeModel testModelYesterday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991959L,
				"USD", null);
		Map<String, BigDecimal> ratesYesterday = new HashMap<>();
		ratesYesterday.put("RUB", new BigDecimal(55.624893).setScale(6, RoundingMode.HALF_EVEN));
		testModelYesterday.setRates(ratesYesterday);


		ExchangeModel testModelToday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991960L,
				"USD", null);
		Map<String, BigDecimal> ratesToday = new HashMap<>();
		ratesToday.put("RUB", new BigDecimal(57.624893).setScale(6, RoundingMode.HALF_EVEN));
		testModelToday.setRates(ratesToday);

		String yesterdayDate = Instant.now().minus(1, ChronoUnit.DAYS)
				.atZone(ZoneOffset.UTC)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
		Mockito.when(exchangeService.getExchangeRates("RUB")).thenReturn(testModelToday);
		Mockito.when(exchangeService.getExchangeRates(yesterdayDate, "RUB")).thenReturn(testModelYesterday);

		String gifSource = "https://media2.giphy.com/media/l0HlBmsbhbk8NqZGw/giphy.gif?cid=0039d4390282e9c17df4c2b255c707a779184646e4080f8a&rid=giphy.gif&ct=g";
		Mockito.when(gifService.getGif("broke")).thenReturn(String.format("{ \"data\" : { \"images\" : { \"downsized_large\": { \"url\": \"%s\" } } } }", gifSource));

		Assert.isTrue(String.format("<img style=\"display: block;\n" +
				"  margin-left: auto;\n" +
				"  margin-right: auto;\n" +
				"  width: 50%% ;\" src=\"%s\">", gifSource).equals(gifController.getGif("RUB")), "Error!");

		Mockito.verify(exchangeService).getExchangeRates("RUB");
		Mockito.verify(exchangeService).getExchangeRates(yesterdayDate, "RUB");

		Mockito.verify(gifService).getGif("broke");
	}

	@Test
	public void testErrorMessage() {
		ExchangeModel testModelYesterday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991959L,
				"USD", new HashMap<>());

		ExchangeModel testModelToday = new ExchangeModel(
				"Usage subject to terms: https://openexchangerates.org/terms",
				"https://openexchangerates.org/license",
				1654991960L,
				"USD", new HashMap<>());

		String yesterdayDate = Instant.now().minus(1, ChronoUnit.DAYS)
				.atZone(ZoneOffset.UTC)
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
		Mockito.when(exchangeService.getExchangeRates("RUBW")).thenReturn(testModelToday);
		Mockito.when(exchangeService.getExchangeRates(yesterdayDate, "RUBW")).thenReturn(testModelYesterday);

		Assert.isTrue(String.format("<b><p style=\"text-align: center" +
				"\">%s<p><b>", "Rate for the given currency wasn't found!").equals(gifController.getGif("RUBW")), "Error!");

		Mockito.verify(exchangeService).getExchangeRates("RUBW");
		Mockito.verify(exchangeService).getExchangeRates(yesterdayDate, "RUBW");
	}
}
