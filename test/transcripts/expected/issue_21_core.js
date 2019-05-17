// js-beautify v1.10.0
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
null != fn_2 && fn_2.apply(call_info_2[0], oops.helpers.to_native_array());
