package org.almibe.codeeditor;

import java.io.IOException;

/*
* Copyright 2010 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License. You may obtain a copy of
* the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations under
* the License.
*/
public class JsonString {
    //this method has been slightly modified to return a String instead of accepting a write in the parameter list.
    public static String quote(String data) {
        StringBuilder writer = new StringBuilder();
        if (data == null) {
            writer.append("null");
            return writer.toString();
        }
        writer.append('"');
        for (int i = 0, n = data.length(); i < n; ++i) {
            final char c = data.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    writer.append('\\').append(c);
                    break;
                case '\b':
                    writer.append("\\b");
                    break;
                case '\t':
                    writer.append("\\t");
                    break;
                case '\n':
                    writer.append("\\n");
                    break;
                case '\f':
                    writer.append("\\f");
                    break;
                case '\r':
                    writer.append("\\r");
                    break;
                default:
                    writer.append(c);
            }
        }
        writer.append('"');
        return writer.toString();
    }
}
