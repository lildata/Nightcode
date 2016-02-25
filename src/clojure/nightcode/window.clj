(ns nightcode.window
  (:require [nightcode.dialogs :as dialogs]
            [nightcode.editors :as editors]
            [nightcode.file-browser :as file-browser]
            [nightcode.git :as git]
            [nightcode.shortcuts :as shortcuts]
            [nightcode.ui :as ui]
            [seesaw.core :as s]
            [seesaw.icon :as i])
  (:import [java.awt Window]
           [java.awt.event WindowAdapter]
           [java.lang.reflect InvocationHandler Proxy]
           [org.pushingpixels.substance.api SubstanceLookAndFeel] ; remove
           [org.pushingpixels.substance.api.skin GraphiteSkin])) ; remove

; remove
(defn set-theme!
  "Sets the theme based on the command line arguments."
  [args]
  (s/native!)
  (let [{:keys [shade skin-object theme-resource]} args]
    (when theme-resource (reset! ui/theme-resource theme-resource))
    (SubstanceLookAndFeel/setSkin (or skin-object (GraphiteSkin.)))))

; remove
(defn show-shut-down-dialog!
  "Displays a dialog confirming whether the program should shut down."
  []
  (dialogs/show-shut-down-dialog! (editors/unsaved-paths)))

; remove
(defn confirm-exit-app!
  "Shuts down unless a quit handler exists or the user cancels it."
  []
  (if (and (nil? (try (Class/forName "com.apple.eawt.QuitHandler")
                   (catch Exception _)))
           (show-shut-down-dialog!))
    (System/exit 0)
    true))

; remove
(defn set-icon!
  "Sets the dock icon on OS X."
  [path]
  (some-> (try (Class/forName "com.apple.eawt.Application")
            (catch Exception _))
          (.getMethod "getApplication" (into-array Class []))
          (.invoke nil (object-array []))
          (.setDockIconImage (.getImage (i/icon path)))))

; remove
(defn enable-full-screen!
  "Enables full screen mode on OS X."
  [window]
  (some-> (try (Class/forName "com.apple.eawt.FullScreenUtilities")
            (catch Exception _))
          (.getMethod "setWindowCanFullScreen"
            (into-array Class [Window Boolean/TYPE]))
          (.invoke nil (object-array [window true]))))

; remove
(defn create-quit-handler
  "Creates an OS X quit handler."
  []
  (when-let [quit-class (try (Class/forName "com.apple.eawt.QuitHandler")
                          (catch Exception _))]
    (Proxy/newProxyInstance (.getClassLoader quit-class)
                            (into-array Class [quit-class])
                            (reify InvocationHandler
                              (invoke [this proxy method args]
                                (when (= (.getName method)
                                         "handleQuitRequestWith")
                                  (if (show-shut-down-dialog!)
                                    (.performQuit (second args))
                                    (.cancelQuit (second args)))))))))

; remove
(defn override-quit-handler!
  "Overrides the default quit handler on OS X."
  []
  (when-let [quit-handler (create-quit-handler)]
    (some-> (try (Class/forName "com.apple.eawt.Application")
              (catch Exception _))
            (.getMethod "getApplication" (into-array Class []))
            (.invoke nil (object-array []))
            (.setQuitHandler quit-handler))))

(defn add-listener!
  "Sets callbacks for window events."
  [window]
  (override-quit-handler!)
  (.addWindowListener window
    (proxy [WindowAdapter] []
      (windowActivated [e]
        ; force hints to hide
        (reset! shortcuts/down? false)
        (shortcuts/toggle-hint! @editors/tabs false)
        (shortcuts/toggle-hints! @ui/root false)
        ; update the project tree and various panes
        (ui/update-project-tree!)
        (file-browser/update-card!)
        (git/update-sidebar!))
      (windowClosing [e]
        (when (show-shut-down-dialog!)
          (System/exit 0))))))
