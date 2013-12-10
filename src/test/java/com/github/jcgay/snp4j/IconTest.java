package com.github.jcgay.snp4j;

import org.junit.Test;

import java.net.URL;

import static com.github.jcgay.snp4j.assertion.SnpAssertions.assertThat;


public class IconTest {

    @Test
    public void should_create_path_based_icon() throws Exception {
        assertThat(Icon.path("path"))
                .hasValue("path")
                .isNotBase64();
    }

    @Test
    public void should_create_url_based_icon() throws Exception {
        assertThat(Icon.url(new URL("http://my.domain.com/icon.png")))
                .hasValue("http://my.domain.com/icon.png")
                .isNotBase64();
    }

    @Test
    public void should_create_snarl_icon_based_icon() throws Exception {
        assertThat(Icon.stock("snarl.icon"))
                .hasValue("!snarl.icon")
                .isNotBase64();
    }

    @Test
    public void should_create_base64_based_icon() throws Exception {
        assertThat(Icon.base64("icon".getBytes()))
                .hasValue("aWNvbg%%")
                .isBase64();
    }
}
