package Tuan2.TranTanPhat.validators;

import Tuan2.TranTanPhat.Entities.User;
import Tuan2.TranTanPhat.Services.UserService;
import Tuan2.TranTanPhat.validators.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userService.findByUsername(username) == null;
    }
}