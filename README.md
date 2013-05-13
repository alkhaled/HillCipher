HillCipher
==========

Cryptanalysis of the HillCipher

Usage: java Hillcipher knownCipherText knownClearText unknownCiphertext

This program finds the key using a known cipher text attack. 
Given a cipher text Ci and cleartext Cl we can obtain the key used to generate Ci through the operation:

key = inverse(Cl) * Ci

this key is then used to decrypt the file.

Currently the program needs the key size to be known to decrypt. It also cannot invert a non-square matrix.
