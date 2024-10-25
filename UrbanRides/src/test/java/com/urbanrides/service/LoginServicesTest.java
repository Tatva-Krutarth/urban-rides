package com.urbanrides.service;

import com.urbanrides.dao.UserRegistrationDao;
import com.urbanrides.dtos.UserRegistrationDto;
import com.urbanrides.helper.CommonValidation;
import com.urbanrides.helper.EmailSend;
import com.urbanrides.model.OtpLogs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServicesTest {

    @Mock
    private CommonValidation commonValidation;

    @Mock
    private UserRegistrationDao userRegistrationDao;

    @InjectMocks
    private LoginService loginServices;

    @Mock
    private UserRegistrationDto userRegistrationDto;

    @Mock
    private OtpLogs otpLogs;

    @Mock
    private EmailSend emailSend;
    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("");
        userRegistrationDto.setOtp("1234");
        userRegistrationDto.setPassword("Krutarth!1");
        userRegistrationDto.setConfPass("Krutarth!1");
        userRegistrationDto.setAcccoutTypeId(2);
        userRegistrationDto.setPhone("3213213211");

        otpLogs = new OtpLogs();
        otpLogs.setGeneratedOtp(1234);
        otpLogs.setEmail("utubekiller1@gmail.com");
        otpLogs.setCreatedDate(LocalDate.now());
        otpLogs.setOptReqSendTime(LocalTime.now().minusMinutes(5));
    }


    @Test
    public void testValidateUserRegistration_InvalidEmail() {
        when(commonValidation.isValidEmail(anyString())).thenReturn(false);

        String result = loginServices.validateUserRegistration(userRegistrationDto);

        assertEquals("Invalid email address", result);
        verify(commonValidation, times(1)).isValidEmail(anyString());
        verify(commonValidation, never()).isValidPassword(anyString());
        verify(commonValidation, never()).confirmPassword(anyString(), anyString());
    }

    @Test
    public void testValidateUserRegistration_InvalidPassword() {
        when(commonValidation.isValidEmail(anyString())).thenReturn(true);
        when(commonValidation.isValidPassword(anyString())).thenReturn(false);

        String result = loginServices.validateUserRegistration(userRegistrationDto);

        assertEquals("Password must contain be between 8 and 13 characters.", result);
        verify(commonValidation, times(1)).isValidEmail(anyString());
        verify(commonValidation, times(1)).isValidPassword(anyString());
        verify(commonValidation, never()).confirmPassword(anyString(), anyString());
    }

    @Test
    public void testValidateUserRegistration_PasswordsDoNotMatch() {
        when(commonValidation.isValidEmail(anyString())).thenReturn(true);
        when(commonValidation.isValidPassword(anyString())).thenReturn(true);
        when(commonValidation.confirmPassword(anyString(), anyString())).thenReturn(false);

        String result = loginServices.validateUserRegistration(userRegistrationDto);

        assertEquals("Passwords do not match", result);
        verify(commonValidation, times(1)).isValidEmail(anyString());
        verify(commonValidation, times(1)).isValidPassword(anyString());
        verify(commonValidation, times(1)).confirmPassword(anyString(), anyString());
    }

    @Test
    public void testValidateUserRegistration_ValidData() {
        when(commonValidation.isValidEmail(anyString())).thenReturn(true);
        when(commonValidation.isValidPassword(anyString())).thenReturn(true);
        when(commonValidation.confirmPassword(anyString(), anyString())).thenReturn(true);

        String result = loginServices.validateUserRegistration(userRegistrationDto);

        assertEquals(null, result);
        verify(commonValidation, times(1)).isValidEmail(anyString());
        verify(commonValidation, times(1)).isValidPassword(anyString());
        verify(commonValidation, times(1)).confirmPassword(anyString(), anyString());
    }


//    -----------------------------------test case of login service ----------------------------------------


    @Test
    public void testOtpService_ValidNewOtpRequest() {
        when(userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail())).thenReturn(null);

        loginServices.otpService(userRegistrationDto);

        verify(userRegistrationDao, times(1)).saveUser(any(OtpLogs.class));
        verify(emailSend, times(1)).userRegistrationOtp(any(UserRegistrationDto.class), anyInt());
    }

    @Test
    public void testOtpService_ValidExistingOtpRequestBeyond2Minutes() {
        otpLogs.setOptReqSendTime(LocalTime.now().minusMinutes(3));
        when(userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail())).thenReturn(otpLogs);

        loginServices.otpService(userRegistrationDto);

        verify(userRegistrationDao, times(1)).saveUser(any(OtpLogs.class));
        verify(emailSend, times(1)).userRegistrationOtp(any(UserRegistrationDto.class), anyInt());
    }

    @Test
    public void testOtpService_ValidExistingOtpRequestWithin2Minutes() {
        otpLogs.setOptReqSendTime(LocalTime.now().minusMinutes(1));
        when(userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail())).thenReturn(otpLogs);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginServices.otpService(userRegistrationDto);
        });

        assertEquals("Please wait for 2 minutes before requesting a new OTP", exception.getMessage());
        verify(userRegistrationDao, never()).saveUser(any(OtpLogs.class));
        verify(emailSend, never()).userRegistrationOtp(any(UserRegistrationDto.class), anyInt());
    }

    @Test
    public void testOtpService_InvalidEmailRegistration() {
        userRegistrationDto.setEmail("invalid-email");
        when(commonValidation.isValidEmail(anyString())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            loginServices.otpService(userRegistrationDto);
        });

        assertEquals("Email not sent", exception.getMessage());
        verify(userRegistrationDao, never()).getOtpLogsByEmail(anyString());
        verify(emailSend, never()).userRegistrationOtp(any(UserRegistrationDto.class), anyInt());
    }

    @Test
    public void testOtpService_EmailSendingFailure() {
        when(userRegistrationDao.getOtpLogsByEmail(userRegistrationDto.getEmail())).thenReturn(null);
        doThrow(new RuntimeException("Email not sent")).when(emailSend).userRegistrationOtp(any(UserRegistrationDto.class), anyInt());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginServices.otpService(userRegistrationDto);
        });

        assertEquals("Email not sent", exception.getMessage());
        verify(userRegistrationDao, times(1)).saveUser(any(OtpLogs.class));
    }
}