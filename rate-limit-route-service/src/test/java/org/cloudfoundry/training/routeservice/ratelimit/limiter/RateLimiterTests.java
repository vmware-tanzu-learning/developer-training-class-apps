package org.cloudfoundry.training.routeservice.ratelimit.limiter;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.cloudfoundry.training.routeservice.ratelimit.record.AccessRecord;
import org.cloudfoundry.training.routeservice.ratelimit.record.AccessRecordRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RateLimiterTests {

	@MockBean
	private AccessRecordRepository repository;
	
	@Test
	public void itIsTrueWhenNoRecord() {
		RateLimiter limiter = new RateLimiter(repository, 10, 60);
		when(repository.findById(anyString())).thenReturn(Optional.empty());
		assertTrue(limiter.isAllowed("clientId"));
	}
	
	@Test
	public void itIsTrueWhenWithinBounds() {
		RateLimiter limiter = new RateLimiter(repository, 10, 60);
		when(repository.findById(anyString())).thenReturn(Optional.of(new AccessRecord("clientId", 1, new Date())));
		assertTrue(limiter.isAllowed("clientId"));
	}
	
	@Test
	public void itIsFalseWhenTooManyRequestsWithinTime() {
		RateLimiter limiter = new RateLimiter(repository, 10, 60);
		when(repository.findById(anyString())).thenReturn(Optional.of(new AccessRecord("clientId", 10, new Date())));
		assertFalse(limiter.isAllowed("clientId"));
	}
	
	@Test
	public void itIsFalseWhenTooManyRequestsWithinTimeButOkForOtherClient() {
		RateLimiter limiter = new RateLimiter(repository, 10, 60);
		when(repository.findById("clientId")).thenReturn(Optional.of(new AccessRecord("clientId", 10, new Date())));
		when(repository.findById("otherClientId")).thenReturn(Optional.of(new AccessRecord("otherClientId", 5, new Date())));
		assertFalse(limiter.isAllowed("clientId"));
		assertTrue(limiter.isAllowed("otherClientId"));
	}
	
	@Test
	public void itIsTrueWhenTimeElapses() {
		RateLimiter limiter = new RateLimiter(repository, 10, 60);
		Date date = new Date();
		date.setTime(date.getTime() - 61000);
		when(repository.findById(anyString())).thenReturn(Optional.of(new AccessRecord("clientId", 20, date)));
		assertTrue(limiter.isAllowed("clientId"));
	}
	
}
