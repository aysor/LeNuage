package ru.netology.cloudservice.utils;

import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.model.entity.UserEntity;

public class ConstEntity {
    private static byte[] fileData = new byte[]{1, 2, 3};

    public static UserEntity USER = new UserEntity(1L, "user1", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

    public static UserEntity USER_222 = new UserEntity(null, "user222", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

    public static UserEntity USER_333 = new UserEntity(null, "user333", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");

    public static FileEntity FILE = new FileEntity(1111L, "tmp111.txt", fileData, (long)fileData.length, USER);

    public static FileEntity FILE_333 = new FileEntity(null, "tmp333.txt", fileData, (long)fileData.length, USER_333);
}
