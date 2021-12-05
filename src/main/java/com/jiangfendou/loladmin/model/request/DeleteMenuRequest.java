package com.jiangfendou.loladmin.model.request;


import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * DeleteMenuRequest.
 * @author jiangmh
 */
@Data
public class DeleteMenuRequest {

    @NotNull
    private Long id;

    @NotNull
    private Integer lockVersion;

    private LocalDateTime deleteDatetime;
}
