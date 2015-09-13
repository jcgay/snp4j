package fr.jcgay.snp4j;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationTest {

    @Test
    public void should_create_application_with_provided_password() {
        Application application = Application.withPassword("id", "name", "provided-password");

        assertThat(application.getPassword()).isEqualTo("provided-password");
    }

    @Test
    public void should_create_application_without_password() {
        Application application = Application.withoutPassword("id", "name");

        assertThat(application.getPassword()).isNull();
    }

    @Test
    public void should_create_application_with_generated_password() {
        Application application = Application.withPassword("id", "name");

        assertThat(application.getPassword()).isNotEmpty();
    }
}