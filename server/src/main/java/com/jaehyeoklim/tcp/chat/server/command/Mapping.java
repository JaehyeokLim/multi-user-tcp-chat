package main.java.com.jaehyeoklim.tcp.chat.server.command;

import java.lang.annotation.*;

/**
 * 명령어와 메서드를 연결(또는 매핑)하기 위한 커스텀 애노테이션.
 *
 * <p>TCP 채팅 서버에서 "/exit", "/message" 같은 명령어를
 * 컨트롤러의 메서드에 매핑할 때 사용된다.
 * 런타임에 리플렉션을 통해 이 애노테이션이 붙은 메서드를 찾아,
 * 명령어(String)를 키로 등록하고 해당 메서드가 실행되도록 한다.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Mapping {
    String value();
}