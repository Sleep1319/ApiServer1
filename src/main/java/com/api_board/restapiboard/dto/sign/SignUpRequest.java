package com.api_board.restapiboard.dto.sign;

import com.api_board.restapiboard.domain.member.Member;
import com.api_board.restapiboard.domain.member.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "회원가입 요청")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @ApiModelProperty(value = "이메일", notes = "이메일을 입력해주세요", required = true, example = "member@email.com")
    @Email(message = "이메일 형식을 다릅니다")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @ApiModelProperty(value = "비밀번호", notes = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.", required = true, example = "123456a!")
    @NotBlank(message = "비밀번호에 공백이 있거나 입력이 안되어있음")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @ApiModelProperty(value = "사용자 이름", notes = "사용자 이름은 한글 또는 알파벳으로 입력해주세요.", required = true, example = "천웅")
    @NotBlank(message = "이름에 공백이 있거나 입력값이 없습니다")
    @Size(min = 2, message = "2글자 이상 적어주십시오")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "사용자 이름은 한글 또는 알파벳만 가능합니다")
    private String username;

    @ApiModelProperty(value = "닉네임", notes = "닉네임은 한글 또는 알파벳으로 입력해주세요.", required = true, example = "잠든웅탱이, Sleep")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message = "2글자 이상 적어주십시오")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 한글 또는 알파벳만 입력해주세요.")
    private String nickname;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder passwordEncoder) {
        return new Member(req.email, passwordEncoder.encode(req.password), req.username, req.nickname, List.of(role));
    }
}
