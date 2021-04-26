/*
 * Copyright (C) 2016 Bartosz Schiller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.barteksc.pdfviewer.source;

import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArraySource implements DocumentSource {

    private static final String TAG = ByteArraySource.class.getName();
    private byte[] data;

    public ByteArraySource(byte[] data) {
        this.data = data;
    }

    @Override
    public PdfRenderer createRenderer(Context context) throws IOException {
        return new PdfRenderer(getFileDescriptor(data));
    }

    private ParcelFileDescriptor getFileDescriptor(byte[] fileData) throws IOException {
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
        InputStream inputStream = new ByteArrayInputStream(fileData);
        ParcelFileDescriptor.AutoCloseOutputStream outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);
        int len;
        while ((len = inputStream.read()) >= 0) {
            outputStream.write(len);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
        return pipe[0];
    }
}
