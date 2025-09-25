package ru.netology.cloudservice.utils;

import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.model.entity.UserEntity;

public class ConstEntity {
    private static byte[] fileData = new byte[]{1, 2, 3};

    public static UserEntity USER = new UserEntity(1L, "user1", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

    public static FileEntity FILE = new FileEntity(1L, "tmp.txt", fileData, (long)fileData.length, USER);
}
