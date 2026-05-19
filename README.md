# PCF Template Installer Plugin

A dedicated plugin for the **Code on the Go** mobile IDE designed to streamline the setup and installation of **Pebble Custom Functions (PCF)** templates. 

This plugin allows developers to quickly create Pebble Custom Function and test the new function by providing installing two templates into Code On The Go.

---

## 🛠 Features
* **One-Tap Installation:** Installs standard PCF templates directly into your Code On The Go project directory.
* **Dependency Management:** Automatically ensures that required libraries for Pebble Custom Functions are correctly referenced.
* **Mobile Optimized:** Built specifically for the "Code on the Go" interface for a smooth developer experience on Android.

## 📦 Installation

### Prerequisites
* **Code on the Go** IDE installed on your Android device.
* An active workspace folder.

### Steps
1.  Open the **Code on the Go** app.
2.  Navigate to the **Plugin Manager** in the side menu.
3.  Select **Install from URL** or search for `PCFTemplateInstallerPlugin`.
4.  Enter the repository URL: `https://github.com/appdevforall/PCFTemplateInstallerPlugin`
5.  Restart the IDE to activate the plugin.

## 🚀 How to Use
1.  Open a project where you want to implement a **Pebble Custom Function**.
2.  Right-click (or long-press) on your desired directory.
3.  Select **"Install PCF Template"** from the context menu.
4.  Choose your template type (e.g., *Data Hook*, *Custom Indicator*, or *UI Logic*).
5.  The plugin will automatically populate the folder with the necessary `.pcf` or `.js` files.

## 🤝 Contributing
We welcome contributions to expand the available PCF templates!
1.  Fork the repository.
2.  Add your template files to the `/templates` directory.
3.  Update the `manifest.json` to include your new template metadata.
4.  Submit a Pull Request for review.

---

### About App Dev For All
We are dedicated to making mobile development accessible to everyone. Check out our other tools and resources at [appdevforall.org](https://www.appdevforall.org).

