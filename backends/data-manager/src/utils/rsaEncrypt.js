import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3PKRVv4AqI9FOxmw5/a4sNR5BrxOQBhdf/Q9d+rM7w+8zUURxm0rSDawH7Q8/4WSZfWV3YYBIXgU3rgLZnV3Ef+cjkgseC4cWmL5G+Fml4ptKtwONpdHEdp6vli0QNZyGvNKQ/h2oKIW9RcBD4FASSau6vleeSp0sfiL/GiF0UQIDAQAB'

const privateKey = 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALc8pFW/gCoj0U7GbDn9riw1HkGvE5AGF1/9D136szvD7zNRRHGbStINrAftDz/hZJl9ZXdhgEheBTeuAtmdXcR/5yOSCx4LhxaYvkb4WaXim0q3A42l0cR2nq+WLRA1nIa80pD+Hagohb1FwEPgUBJJq7q+V55KnSx+Iv8aIXRRAgMBAAECgYBPhClsbKYZo2F5ZfIhWEz1MjfnGSrMHViXdyHsDpcpIGX1RxcElfdqIDS/W8h6WD1nvlsB63XX8bUh7ImgFgJSPjSlKpv4D+Rlr4G3LlOcf79JkSMvM8ichShG6d2UiUGejiy5C4UMxDnDigQbMNpDxwbFIeAtfbZHlEzuQE9GgQJBAPRqNsjE8G6oMS8O2foQlMHooTKAJP1qyYdpAtfIhbG11CxB/cPYqe3ZAgJRLbTJcDlaVjPlNGH9sPptHh/pSTUCQQC/7BR4nH7g9ARqH2pBqa453iUoOWDRYNtFAP93IhbNurNJY1vruO26JdtGPt08iyDKu22JUad/Tv+PVe/WKf4tAkBID1CbnMSuO1RU5XIKsfzJtsZf1zwQov1LuQAnkm0oWc12RvaEWUqwXzyiZkCtfn5hymqD3l21TlBxZN5u1h7JAkEAomXCgLhziCgTIDVK9Rr+SQsXR+0dZq5dfAs3H/ihcR3dHWlM266UwiHMZKlHDTbw3XsxWbPVVp1jFEd0dTT9VQJBALU9pcWYixh7K6DkCK2i4kO5BH8cKT1nxvZC8RtyVY4+YeyouGygUB8QWGHX7ratrOppRGc4dy9tlwy5iiNi2Cw='

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

// 解密
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey)
  return encryptor.decrypt(txt)
}