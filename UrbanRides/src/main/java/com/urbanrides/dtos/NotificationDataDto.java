package com.urbanrides.dtos;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NotificationDataDto {
    private String notificationTypeId;
    private String notificationType;
    private String createdDate;
    private String notificationMsg;
}
