// js-beautify v1.13.5
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/issue_21.cljs [core]
//   {:elide-asserts true,
//    :external-config
//    #:devtools{:config {:silence-optimizations-warning true}},
//    :main oops.arena.issue-21,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/issue-21-core/_workdir",
//    :output-to "test/resources/.compiled/issue-21-core/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "exercise oget with *warn-on-infer* enabled"
//     (oget js/document :foo))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// SNIPPET #2:
//   (testing "exercise oset! with *warn-on-infer* enabled"
//     (oset! js/document :test-issue-21 "foo"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

document["test-issue-21"] = "foo";

// SNIPPET #3:
//   (testing "exercise ocall! with *warn-on-infer* enabled"
//     (ocall! js/document :getElementById "foo"))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_1 = document,
  call_info_1 = [target_obj_1, target_obj_1.getElementById],
  fn_1 = call_info_1[1];
null != fn_1 && fn_1.call(call_info_1[0], "foo");

// SNIPPET #4:
//   (testing "exercise oapply! with *warn-on-infer* enabled"
//     (oapply! js/document :getElementById ["foo"]))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var target_obj_2 = document,
  call_info_2 = [target_obj_2, target_obj_2.getElementById],
  fn_2 = call_info_2[1];
if (null != fn_2) {
  var JSCompiler_temp_const7 = fn_2.apply,
    JSCompiler_temp_const8 = call_info_2[0],
    JSCompiler_inline_result9;
  a: {
    var collinline_1 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector$EMPTY_NODE, ["foo"], null);
    if (cljs.core.array_QMARK_(collinline_1)) JSCompiler_inline_result9 = collinline_1;
    else
      for (var arrinline_1 = [], itemsinline_1 = cljs.core.seq(collinline_1);;)
        if (null != itemsinline_1) {
          var iteminline_1 = cljs.core._first(itemsinline_1);
          arrinline_1.push(iteminline_1);
          itemsinline_1 = cljs.core.next(itemsinline_1)
        } else {
          JSCompiler_inline_result9 = arrinline_1;
          break a
        }
  }
  JSCompiler_temp_const7.call(fn_2, JSCompiler_temp_const8, JSCompiler_inline_result9)
}
