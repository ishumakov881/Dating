package com.lds.quickdeal.repository

import java.io.InputStream


//Throwing OutOfMemoryError "Failed to allocate a 32 byte allocation with 4766688 free bytes and 4654KB until OOM, target footprint 268435456, growth limit 268435456; failed due to fragmentation (largest possible contiguous allocation 0 bytes)" (VmSize 1527188 kB, recursive case)

//class ProgressInputStream(
//    private val inputStream: InputStream,
//    private val totalBytes: Long,
//    private val onProgress: (Long, Long) -> Unit
//) : InputStream() {
//
//    private var bytesRead = 0L
//    private val buffer = ByteArray(8192) // 8 KB buffer for more efficient reads
//
//    override fun read(): Int {
//        // Read one byte at a time (same as before)
//        val byte = inputStream.read()
//        if (byte != -1) {
//            bytesRead++
//            onProgress(bytesRead, totalBytes)
//        }
//        return byte
//    }
//
//    override fun read(b: ByteArray, off: Int, len: Int): Int {
//        // Read data into the buffer first
//        val bytesReadFromBuffer = inputStream.read(buffer, 0, minOf(buffer.size, len))
//
//        if (bytesReadFromBuffer > 0) {
//            // Copy from the buffer to the provided array
//            System.arraycopy(buffer, 0, b, off, bytesReadFromBuffer)
//            bytesRead += bytesReadFromBuffer
//            onProgress(bytesRead, totalBytes)
//        }
//
//        return bytesReadFromBuffer
//    }
//
//    override fun close() {
//        inputStream.close()
//    }
//}


class ProgressInputStream(
    private val inputStream: InputStream,
    private val totalBytes: Long,
    private val onProgress: (Long, Long) -> Unit
) : InputStream() {

    private var bytesRead = 0L

    override fun read(): Int {
        val byte = inputStream.read()
        if (byte != -1) {
            bytesRead++
            onProgress(bytesRead, totalBytes)
        }
        return byte
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val count = inputStream.read(b, off, len)
        if (count > 0) {
            bytesRead += count
            onProgress(bytesRead, totalBytes)
        }
        return count
    }

    override fun close() {
        inputStream.close()
    }
}
