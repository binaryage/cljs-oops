// Clojure v1.9.0-alpha13, ClojureScript v1.9.229
// ----------------------------------------------------------------------------------------------------------
// COMPILER CONFIG:
//   arena/ocall_dynamic.cljs [goog]
//   {:elide-asserts true,
//    :external-config #:oops{:config {:key-set :goog, :key-get :goog}},
//    :main oops.arena.ocall-dynamic,
//    :optimizations :advanced,
//    :output-dir "test/resources/.compiled/ocall-dynamic-goog/_workdir",
//    :output-to "test/resources/.compiled/ocall-dynamic-goog/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------

// SNIPPET #1:
//   (testing "simple dynamic ocall"
//     (ocall+ #js {"f" (fn [] 42)} (identity "f")))
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

var $call_info_37$$ = function($obj$$10$$, $selector$$3$$) {
    var $path_4$$ = $oops$core$build_path_dynamically$$($selector$$3$$),
      $len_5$$ = $path_4$$.length;
    if (4 > $len_5$$) return [$obj$$10$$, function() {
      for (var $selector$$3$$ = $path_4$$.length, $len_5$$ = 0, $target_obj_6$$ = $obj$$10$$;;)
        if ($len_5$$ < $selector$$3$$) {
          var $mode_3$$ = $path_4$$[$len_5$$],
            $key_23$$ = $path_4$$[$len_5$$ + 1],
            $next_obj_9$$ = $goog$object$get$$($target_obj_6$$, $key_23$$);
          switch ($mode_3$$) {
            case 0:
              $target_obj_6$$ = $next_obj_9$$;
              $len_5$$ += 2;
              continue;
            case 1:
              if (null != $next_obj_9$$) {
                $target_obj_6$$ = $next_obj_9$$;
                $len_5$$ += 2;
                continue
              } else return null;
            case 2:
              null != $next_obj_9$$ ? ($target_obj_6$$ = $next_obj_9$$, $len_5$$ += 2) : ($len_5$$ += 2, $target_obj_6$$ = $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$($target_obj_6$$, $key_23$$) :
                $oops$core$punch_key_dynamically_BANG_$$.call(null, $target_obj_6$$, $key_23$$));
              continue;
            default:
              throw Error([$cljs$core$str$$("No matching clause: "), $cljs$core$str$$($mode_3$$)].join(""));
          }
        } else return $target_obj_6$$
    }()];
    var $target_obj_6$$ = function() {
      for (var $selector$$3$$ = $path_4$$.slice(0, $len_5$$ - 2), $target_obj_6$$ = $selector$$3$$.length, $G__24_i_11$$ = 0, $G__25_G__26_G__27_G__28_obj_29$$ = $obj$$10$$;;)
        if ($G__24_i_11$$ < $target_obj_6$$) {
          var $mode_13$$ =
            $selector$$3$$[$G__24_i_11$$],
            $key_14$$ = $selector$$3$$[$G__24_i_11$$ + 1],
            $next_obj_15$$ = $goog$object$get$$($G__25_G__26_G__27_G__28_obj_29$$, $key_14$$);
          switch ($mode_13$$) {
            case 0:
              $G__25_G__26_G__27_G__28_obj_29$$ = $next_obj_15$$;
              $G__24_i_11$$ += 2;
              continue;
            case 1:
              if (null != $next_obj_15$$) {
                $G__25_G__26_G__27_G__28_obj_29$$ = $next_obj_15$$;
                $G__24_i_11$$ += 2;
                continue
              } else return null;
            case 2:
              null != $next_obj_15$$ ? ($G__25_G__26_G__27_G__28_obj_29$$ =
                $next_obj_15$$, $G__24_i_11$$ += 2) : ($G__24_i_11$$ += 2, $G__25_G__26_G__27_G__28_obj_29$$ = $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$($G__25_G__26_G__27_G__28_obj_29$$, $key_14$$) : $oops$core$punch_key_dynamically_BANG_$$.call(null, $G__25_G__26_G__27_G__28_obj_29$$, $key_14$$));
              continue;
            default:
              throw Error([$cljs$core$str$$("No matching clause: "),
                $cljs$core$str$$($mode_13$$)
              ].join(""));
          }
        } else return $G__25_G__26_G__27_G__28_obj_29$$
    }();
    return [$target_obj_6$$, function() {
      for (var $obj$$10$$ = [$path_4$$[$len_5$$ - 2], $path_4$$[$len_5$$ - 1]], $selector$$3$$ = $obj$$10$$.length, $G__30_i_16$$ = 0, $G__31_G__32_G__33_G__34_obj_13$$ = $target_obj_6$$;;)
        if ($G__30_i_16$$ < $selector$$3$$) {
          var $mode_18$$ = $obj$$10$$[$G__30_i_16$$],
            $key_19$$ = $obj$$10$$[$G__30_i_16$$ + 1],
            $next_obj_35$$ =
            $goog$object$get$$($G__31_G__32_G__33_G__34_obj_13$$, $key_19$$);
          switch ($mode_18$$) {
            case 0:
              $G__31_G__32_G__33_G__34_obj_13$$ = $next_obj_35$$;
              $G__30_i_16$$ += 2;
              continue;
            case 1:
              if (null != $next_obj_35$$) {
                $G__31_G__32_G__33_G__34_obj_13$$ = $next_obj_35$$;
                $G__30_i_16$$ += 2;
                continue
              } else return null;
            case 2:
              null != $next_obj_35$$ ? ($G__31_G__32_G__33_G__34_obj_13$$ = $next_obj_35$$, $G__30_i_16$$ += 2) : ($G__30_i_16$$ +=
                2, $G__31_G__32_G__33_G__34_obj_13$$ = $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$ ? $oops$core$punch_key_dynamically_BANG_$$.$cljs$core$IFn$_invoke$arity$10$($G__31_G__32_G__33_G__34_obj_13$$, $key_19$$) : $oops$core$punch_key_dynamically_BANG_$$.call(null, $G__31_G__32_G__33_G__34_obj_13$$, $key_19$$));
              continue;
            default:
              throw Error([$cljs$core$str$$("No matching clause: "), $cljs$core$str$$($mode_18$$)].join(""));
          }
        } else return $G__31_G__32_G__33_G__34_obj_13$$
    }()]
  }({
      f: function() {
        return 42
      }
    },
    "f"),
  $fn_38$$ = $call_info_37$$[1];
null != $fn_38$$ && $fn_38$$.call($call_info_37$$[0]);
