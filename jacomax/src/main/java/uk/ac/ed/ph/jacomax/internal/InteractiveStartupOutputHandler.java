/* Copyright (c) 2010 - 2012, The University of Edinburgh.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 * 
 * * Neither the name of the University of Edinburgh nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.ed.ph.jacomax.internal;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;

/**
 * Handler for Maxima output generated during interactive startup.
 *
 * @author David McKain
 */
public class InteractiveStartupOutputHandler extends InteractiveOutputHandler {

    private final StringBuilder lastOutputLineBuilder;

    public InteractiveStartupOutputHandler(final ByteBuffer decodingByteBuffer,
            final CharBuffer decodingCharBuffer, final CharsetDecoder charsetDecoder) {
        super(decodingByteBuffer, decodingCharBuffer, charsetDecoder);
        this.lastOutputLineBuilder = new StringBuilder();
    }

    @Override
    public void callStarting() {
        super.callStarting();
    }

    @Override
    protected void handleDecodedOutputChunk(final CharBuffer buffer) {
        /* Build up current line so we can check when we're at the required terminator */
        while (buffer.hasRemaining()) {
            final char c = buffer.get();
            if (c=='\n' || c=='\r') {
                lastOutputLineBuilder.setLength(0);
            }
            else {
                lastOutputLineBuilder.append(c);
            }
        }
    }

    @Override
    protected boolean isNextInputPromptReached() {
        return "(%i1) ".equals(lastOutputLineBuilder.toString());
    }
}