package dev.nick.library.io.procotol;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dev.nick.library.model.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Tornaco on 2017/7/19.
 * Licensed with Apache.
 */

@Builder
@Getter
@ToString
public class DataHeader {

    private long size;
    @NonNull
    private String name;
    @Nullable
    private String extra;
    private
    @NonNull
    Category category;

    public static void writeTo(@NonNull OutputStream outputStream, DataHeader header) throws IOException {
        sizeTo(outputStream, header.getSize());
        nameTo(outputStream, Preconditions.checkNotNull(header.getName()));
        categoryTo(outputStream, Preconditions.checkNotNull(header.getCategory()));
        extraTo(outputStream, Optional.fromNullable(header.getExtra()).or(""));
    }

    public static DataHeader readFrom(@NonNull InputStream in) throws IOException {
        return DataHeader
                .builder()
                .size(sizeOf(in))
                .name(nameOf(in))
                .category(categoryOf(in))
                .extra(extraOf(in))
                .build();
    }

    private static long sizeOf(@NonNull InputStream in) throws IOException {
        byte[] sizeBuffer = new byte[Longs.BYTES];
        if (sizeBuffer.length != in.read(sizeBuffer)) {
            throw new IOException("Invalid file header.");
        }
        return Longs.fromByteArray(sizeBuffer);
    }

    private static void sizeTo(@NonNull OutputStream outputStream, long size) throws IOException {
        byte[] bytes = Longs.toByteArray(size);
        outputStream.write(bytes);
    }

    private static Category categoryOf(@NonNull InputStream in) throws IOException {
        byte[] cateBuffer = new byte[Ints.BYTES];
        if (cateBuffer.length != in.read(cateBuffer)) {
            throw new IOException("Invalid file header.");
        }
        return Category.valueOf(Ints.fromByteArray(cateBuffer));
    }

    private static void categoryTo(@NonNull OutputStream outputStream, Category category) throws IOException {
        byte[] bytes = Ints.toByteArray(category.ordinal());
        outputStream.write(bytes);
    }

    private static String nameOf(@NonNull InputStream in) throws IOException {
        byte[] nameLengthBuffer = new byte[Ints.BYTES];
        if (nameLengthBuffer.length != in.read(nameLengthBuffer)) {
            throw new IOException("Invalid file header.");
        }
        int nameLength = Ints.fromByteArray(nameLengthBuffer);
        byte[] nameBuffer = new byte[nameLength];
        if (nameBuffer.length != in.read(nameBuffer)) {
            throw new IOException("Invalid file header.");
        }
        return new String(nameBuffer);
    }

    private static void nameTo(@NonNull OutputStream outputStream, @NonNull String name) throws IOException {
        // Length.
        int length = name.getBytes().length;
        byte[] bytes = Ints.toByteArray(length);
        outputStream.write(bytes);
        // Name content.
        outputStream.write(name.getBytes());
    }

    private static String extraOf(@NonNull InputStream in) throws IOException {
        byte[] extraLengthBuffer = new byte[Ints.BYTES];
        if (extraLengthBuffer.length != in.read(extraLengthBuffer)) {
            throw new IOException("Invalid file header.");
        }
        int extraLength = Ints.fromByteArray(extraLengthBuffer);
        byte[] extraBuffer = new byte[extraLength];
        if (extraBuffer.length != in.read(extraBuffer)) {
            throw new IOException("Invalid file header.");
        }
        return new String(extraBuffer);
    }

    private static void extraTo(@NonNull OutputStream outputStream, @NonNull String extra) throws IOException {
        // Length.
        int length = extra.getBytes().length;
        byte[] bytes = Ints.toByteArray(length);
        outputStream.write(bytes);
        // Extra content.
        outputStream.write(extra.getBytes());
    }
}
