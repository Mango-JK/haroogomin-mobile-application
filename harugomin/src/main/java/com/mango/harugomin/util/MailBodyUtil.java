package com.mango.harugomin.util;

public class MailBodyUtil {
    public String getMailBody(String nickname, String password){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n" +
                "  <body>\n" +
                "    <table cellspacing=\"0\" cellpadding=\"0\" width=\"712\">\n" +
                "      <tbody>\n" +
                "        <tr>\n" +
                "          <td\n" +
                "            style=\"\n" +
                "              padding-top: 56px;\n" +
                "              padding-bottom: 22px;\n" +
                "              padding-left: 80px;\n" +
                "              font-size: 0;\n" +
                "            \"\n" +
                "          >\n" +
                "            <img src=\"http://15.165.183.122/hago02210103115007.png\"\n" +
                "            alt=\"HARUGOMIN MOBILE APPLICATION\" loading=\"lazy\" width=\"600px\"\n" +
                "            height=\"300px\" display: block; margin: 0px auto; />\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td style=\"vertical-align: top\">\n" +
                "            <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n" +
                "              <tbody>\n" +
                "                <tr>\n" +
                "                  <td\n" +
                "                    style=\"\n" +
                "                      vertical-align: top;\n" +
                "                      padding-top: 48px;\n" +
                "                      width: 48px;\n" +
                "                      background: #fff;\n" +
                "                    \"\n" +
                "                  >\n" +
                "                    <table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\n" +
                "                      <tbody>\n" +
                "                        <tr>\n" +
                "                          <td\n" +
                "                            style=\"\n" +
                "                              font-family: '맑은 고딕', 'malgun gothic', 돋움,\n" +
                "                                dotum, sans-serif;\n" +
                "                              font-size: 24px;\n" +
                "                              font-weight: bold;\n" +
                "                              color: #3c90e2;\n" +
                "                              text-align: center;\n" +
                "                            \"\n" +
                "                          >\n" +
                "                            임시 비밀번호 생성\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                          <td\n" +
                "                            style=\"\n" +
                "                              text-align: center;\n" +
                "                              line-height: 0;\n" +
                "                              padding-top: 25px;\n" +
                "                              padding-bottom: 25px;\n" +
                "                            \"\n" +
                "                          ></td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                          <td\n" +
                "                            style=\"\n" +
                "                              font-family: '맑은 고딕', 'malgun gothic', 돋움,\n" +
                "                                dotum, sans-serif;\n" +
                "                              font-size: 18px;\n" +
                "                              mso-line-height-rule: exactly;\n" +
                "                              line-height: 25px;\n" +
                "                              color: #6e7c8c;\n" +
                "                              text-align: center;\n" +
                "                            \"\n" +
                "                          >\n" +
                "                            안녕하세요\n" +
                "                            <strong style=\"color: #24282b\">"+nickname+"</strong>님,\n" +
                "                            <br />귀하께서 요청하신 임시 비밀번호 <br />수신을\n" +
                "                            위해 발송된 메일입니다. <br /><br />\n" +
                "                            고객님의 임시 비밀번호는\n" +
                "                            <span style=\"color: #3c90e2\">"+ password +"</span\n" +
                "                            >입니다.<br /><br />로그인 후에는 새로운 비밀번호로\n" +
                "                            변경하셔야 합니다.<br />감사합니다.<br /><br /><br />\n" +
                "                          </td>\n" +
                "                        </tr>\n" +
                "                      </tbody>\n" +
                "                    </table>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>\n");
        return sb.toString();
    }
}
