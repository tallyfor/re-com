(ns re-com.tabs
   (:require [clojure.set  :refer [superset?]]
             [re-com.util  :refer [deref-or-value validate-arguments]]
             [reagent.core :as    reagent]))



;;--------------------------------------------------------------------------------------------------
;; Component: horizontal-tabs
;;--------------------------------------------------------------------------------------------------

(def tabs-args-desc
  [{:name :model     :required true  :type "a tab id | atom" :description "the :id of the currently selected tab."}
   {:name :tabs      :required true  :type "vector of maps"  :description "one element in the vector for each tab. In each map, at least :id and :label."}
   {:name :on-change :required true  :type "(:id) -> nil"    :description "called when user alters the selection. Passed the :id of the selection."}])


(def tabs-args
  (set (map :name tabs-args-desc)))


(defn horizontal-tabs
  [& {:keys [model tabs on-change]
      :as   args}]
  {:pre [(validate-arguments tabs-args (keys args))]}
  (let [current  (deref-or-value model)
        tabs     (deref-or-value tabs)
        _        (assert (not-empty (filter #(= current (:id %)) tabs)) "model not found in tabs vector")]
    [:ul
     {:class "rc-tabs nav nav-tabs"
      :style {:flex                "none"
              :-webkit-user-select "none"}}
     (for [t tabs]
       (let [id        (:id t)
             label     (:label t)
             selected? (= id current)]                   ;; must use current instead of @model to avoid reagent warnings
         [:li
          {:class  (if selected? "active")
           :key    (str id)}
          [:a
           {:style     {:cursor "pointer"}
            :on-click  #(on-change id)}
           label]]))]))


;;--------------------------------------------------------------------------------------------------
;; Component: horizontal-bar-tabs
;;--------------------------------------------------------------------------------------------------
(defn- bar-tabs
  [& {:keys [model tabs on-change vertical?]
      :as   args}]
  (let [current  (deref-or-value model)
        tabs     (deref-or-value tabs)
        _        (assert (not-empty (filter #(= current (:id %)) tabs)) "model not found in tabs vector")]
    [:div
     {:class (str "rc-tabs btn-group" (if vertical? "-vertical"))
      :style {:flex                "none"
              :-webkit-user-select "none"}}
     (for [t tabs]
       (let [id        (:id t)
             label     (:label t)
             selected? (= id current)]                    ;; must use current instead of @model to avoid reagent warnings
         [:button.btn.btn-default
          {:type     "button"
           :key      (str id)
           :class    (str "btn btn-default "  (if selected? "active"))
           :on-click  #(on-change id)}
          label]))]))


(defn horizontal-bar-tabs
  [& {:keys [model tabs on-change] :as args}]
  {:pre [(superset? tabs-args (keys args))]}
  (bar-tabs
    :model model
    :tabs tabs
    :on-change on-change
    :vertical? false))

(defn vertical-bar-tabs
  [& {:keys [model tabs on-change] :as args}]
  {:pre [(superset? tabs-args (keys args))]}
  (bar-tabs
    :model model
    :tabs tabs
    :on-change on-change
    :vertical? true))


;;--------------------------------------------------------------------------------------------------
;; Component: pill-tabs
;;--------------------------------------------------------------------------------------------------

(defn- pill-tabs    ;; tabs-like in action
  [& {:keys [model tabs on-change vertical?]}]
  (let [current  (deref-or-value model)
        tabs     (deref-or-value tabs)
        _        (assert (not-empty (filter #(= current (:id %)) tabs)) "model not found in tabs vector")]
    [:ul
     {:class (str "rc-tabs nav nav-pills" (when vertical? " nav-stacked"))
      :style {:flex                "none"
              :-webkit-user-select "none"}
      :role  "tabslist"}
     (for [t tabs]
       (let [id        (:id t)
             label     (:label t)
             selected? (= id current)]                   ;; must use 'current' instead of @model to avoid reagent warnings
         [:li
          {:class    (if selected? "active" "")
           :key      (str id)}
          [:a
           {:style     {:cursor "pointer"}
            :on-click  #(on-change id)}
           label]]))]))


(defn horizontal-pill-tabs
  [& {:keys [model tabs on-change] :as args}]
  {:pre [(superset? tabs-args (keys args))]}
  (pill-tabs
    :model model
    :tabs tabs
    :on-change on-change
    :vertical? false))


(defn vertical-pill-tabs
  [& {:keys [model tabs on-change] :as args}]
  {:pre [(superset? tabs-args (keys args))]}
  (pill-tabs
    :model model
    :tabs tabs
    :on-change on-change
    :vertical? true))
