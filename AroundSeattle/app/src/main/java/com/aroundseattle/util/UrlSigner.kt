package com.aroundseattle.util

import android.util.Base64
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class UrlSigner @Throws(IOException::class)
constructor(keyString: String) {

    // This variable stores the binary key, which is computed from the string (Base64) key
    private val key: ByteArray

    init {
        // Convert the key from 'web safe' base 64 to binary
        var ks = keyString.replace('-', '+')
        ks = ks.replace('_', '/')
        this.key = Base64.decode(ks, android.util.Base64.DEFAULT)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, UnsupportedEncodingException::class, URISyntaxException::class)
    fun signRequest(path: String, query: String): String {

        // Retrieve the proper URL components to sign
        val resource = path + '?'.toString() + query

        // Get an HMAC-SHA1 signing key from the raw key bytes
        val sha1Key = SecretKeySpec(key, "HmacSHA1")

        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(sha1Key)

        // compute the binary signature for the request
        val sigBytes = mac.doFinal(resource.toByteArray())

        // base 64 encode the binary signature
        var signature = Base64.encodeToString(sigBytes, android.util.Base64.DEFAULT)

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-')
        signature = signature.replace('/', '_')

        return "$resource&signature=$signature"
    }

}