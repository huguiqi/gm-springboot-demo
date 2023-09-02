package com.example.demo;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/09/03/00:13
 * @summary:
 */
@SpringBootTest
public class TestJaspyer {


    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void encode() {
        System.out.println("加密密文：" + stringEncryptor.encrypt("abc123"));

        System.out.println("解密密文：" + stringEncryptor.decrypt("AJKoFxhv2cR1RzSXyRjDDw=="));
    }
}
