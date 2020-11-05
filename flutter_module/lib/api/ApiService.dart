import 'dart:convert';

import 'package:flutter_module/bean/Bean.dart';
import 'package:flutter_module/bean/Error.dart';
import 'package:flutter_module/utils/AppManager.dart';
import 'package:flutter_module/utils/HttpConstant.dart';
import 'package:http/http.dart' as http;

class ApiService {
  ApiService._();

  static final base_url = "https://wanandroid.com/";

  static final tree_url = "${base_url}tree/json";

  static final tree_detail_url = "${base_url}article/list/";

  static final login_url = "${base_url}user/login";

  static getData(String url,
      {Function success, Function fail, Function complete}) async {
    try {
      var response = await http.get(url, headers: HttpConstant.httpHeader);
      if (response.statusCode == 200) {
        var result =
            json.decode(AppManager.utf8decoder.convert(response.bodyBytes));
        var baseResponse = ApiResponse.fromJson(result);
        if (baseResponse != null) {
          switch (baseResponse.errorCode) {
            case 0:
              success(baseResponse.data);
              break;
            case 1001:
              ErrorBean error = ErrorBean("未登录", 1001);
              fail(error);
              break;
          }
        } else {
          ErrorBean error = ErrorBean("数据解析错误", 101);
          fail(error);
        }
      } else {
        throw Exception('"Request failed with status: ${response.statusCode}"');
      }
    } catch (e) {
      ErrorBean error = ErrorBean(e.toString(), 2000);
      fail(error);
    } finally {
      if (complete != null) {
        complete();
      }
    }
  }

  static postData(String url, Map<String, Object> maps,
      {Function success, Function fail, Function complete}) async {
    try {
      var response = await http.post(url, body: maps,encoding: Utf8Codec());
      if (response.statusCode == 200) {
        var result =
        json.decode(AppManager.utf8decoder.convert(response.bodyBytes));
        var baseResponse = ApiResponse.fromJson(result);
        if (baseResponse != null) {
          switch (baseResponse.errorCode) {
            case 0:
              success(baseResponse.data);
              break;
            case 1001:
              ErrorBean error = ErrorBean("未登录", 1001);
              fail(error);
              break;
          }
        } else {
          ErrorBean error = ErrorBean("数据解析错误", 101);
          fail(error);
        }
      } else {
        throw Exception('"Request failed with status: ${response.statusCode}"');
      }
    } catch (e) {
      ErrorBean error = ErrorBean(e.toString(), 2000);
      fail(error);
    } finally {
      if (complete != null) {
        complete();
      }
    }
  }
}
