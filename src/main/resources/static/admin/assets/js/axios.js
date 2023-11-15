<!DOCTYPE html>
<!--suppress HtmlUnknownTarget -->
<html lang="en">
<head>
    <title>Code With Me - JetBrains</title>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, maximum-scale=1">

    <link rel="apple-touch-icon" sizes="180x180" href="/static/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/static/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/static/favicon-16x16.png">
    <link rel="mask-icon" href="/static/safari-pinned-tab.svg" color="#000000">
    <link rel="manifest" href="/static/site.webmanifest">
    <meta name="apple-mobile-web-app-title" content="JetBrains">
    <meta name="application-name" content="JetBrains">
    <meta name="msapplication-TileColor" content="#000000">
    <meta name="theme-color" content="#000000">
    <!--suppress JSUnresolvedLibraryURL -->
    <link
            rel="stylesheet"
            type="text/css"
            href="https://resources.jetbrains.com/storage/ui/fonts/fonts.css"
    />
    <link
            rel="stylesheet"
            type="text/css"
            href="/static/landing.css"
    />
    <script>
        if (!String.prototype.endsWith) {
            Object.defineProperty(String.prototype, 'endsWith', {
                value: function(searchString, position) {
                    let subjectString = this.toString();
                    if (position === undefined || position > subjectString.length) {
                        position = subjectString.length;
                    }
                    position -= searchString.length;
                    let lastIndex = subjectString.indexOf(searchString, position);
                    return lastIndex !== -1 && lastIndex === position;
                }
            });
        }

        // Element.closest() polyfill
        (function(ELEMENT) {
            ELEMENT.matches = ELEMENT.matches || ELEMENT.mozMatchesSelector || ELEMENT.msMatchesSelector || ELEMENT.oMatchesSelector || ELEMENT.webkitMatchesSelector;
            ELEMENT.closest = ELEMENT.closest || function closest(selector) {
                if (!this) return null;
                if (this.matches(selector)) return this;
                if (!this.parentElement) {return null}
                else return this.parentElement.closest(selector)
            };
        }(Element.prototype));

        function supportGateway() {
            return window.location.href.toLowerCase().indexOf("gateway=true") > -1;
        }

        function supportToolbox() {
            return "false".toLowerCase() === "true"
        }

        function useOldLaunchers() {
            return "true".toLowerCase() === "true"
        }

        /**
         * @param {number} port
         * @param {XMLHttpRequest} versionRequest
         */
        function isGoodVersion(port, versionRequest) {
            const minimumRequiredVersion = 4;

            if (versionRequest.status !== 200) {
                console.info(port + ": non-200 http status " + versionRequest.status)
                return false
            }

            if (!versionRequest.responseText.match(/^\d+$/)) {
                console.info(port + ": could not convert response to number (possibly older format): " + versionRequest.responseText)
                return false
            }

            let remoteVersion = parseInt(versionRequest.responseText)
            if (remoteVersion < minimumRequiredVersion) {
                console.info(port + ": version " + remoteVersion + ", but minimum supported is " + minimumRequiredVersion)
                return false
            }

            console.info(port + ": version " + remoteVersion + ": OK")
            return true
        }

        function startup() {
            if (supportToolbox()) {

                // this does exactly what we need:
                // 1) remove the Please wait
                // 2) provide shell scripts / windows exe launchers
                onAllPortsFailed();

                const toolboxEl = document.getElementById('toolbox-block');
                toolboxEl.style.display = ''

                return
            }

            if (supportGateway()) {
                let gatewayUrl = "jetbrains-gateway://cwm#" + window.location.href
                console.log("Using JetBrains Gateway");
                console.log("Open: " + gatewayUrl);
                window.open(gatewayUrl, "_self");
                suggestToDownloadGateway()
                return
            }

            console.info("Connecting to open JetBrains IDE instances...")

            const basePort = 63342
            let processed = false;

            const portCount = 10;
            let failedPorts = 0;

            for (let i = 0; i < portCount; i++) {
                let port = basePort + i
                let versionRequest = new XMLHttpRequest();
                versionRequest.onreadystatechange = function () {
                    if (versionRequest.readyState === 4) {
                        console.info(port + ": " + versionRequest.responseText)
                        if (processed) {
                            console.info(port + ": skipped since one IDE already have a compatible version")
                        } else if (isGoodVersion(port, versionRequest)) {
                            processed = true
                            connect(port)
                        } else {
                            failedPorts++;
                            if (failedPorts === portCount) {
                                console.info("Currently running compatible IDE is not found, asking user to download launcher")
                                onAllPortsFailed();
                            }
                        }
                    }
                }
                versionRequest.open("POST", "http://localhost:" + port + "/codeWithMe/queryThinClient", true);
                versionRequest.send("https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg#p=IC&fp=39867152ABB960DC760E9F00BFEAF1E4E215998DA99D4A90E355F7742CA5B7C7");
            }
        }

        function removeElement(element) {
            element.parentNode.removeChild(element);
        }

        function initCodeBlock(cmdElement, clipboardTrigger) {
            function selectCmd() {
                const range = document.createRange();
                range.selectNode(cmdElement);
                window.getSelection().removeAllRanges();
                window.getSelection().addRange(range);
            }


            clipboardTrigger.addEventListener('mouseleave', function() {
                clipboardTrigger.removeAttribute('aria-label');
            });

            clipboardTrigger.addEventListener('click', function() {
                try {
                    selectCmd();
                    if (document.execCommand('copy')) {
                        clipboardTrigger.setAttribute('aria-label', 'Copied');
                    } else {
                        clipboardTrigger.setAttribute('aria-label', 'Unable to copy')
                    }
                } catch (err) {
                    clipboardTrigger.setAttribute('aria-label', 'Unsupported Browser')
                }
            });
            cmdElement.addEventListener('click', selectCmd);
        }

        function initLinuxSourceCodeBlock() {
            const sourceCodeElement = document.getElementById("linux-case-source-code");
            const cmdElement = sourceCodeElement.querySelector('code')
            // const OneTimeToken=""
            if (isMac()) {
                // No wget on Mac OS X, only curl
                cmdElement.innerText = "/bin/bash -c \"$(curl -fsSL 'https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg/cwm-client-launcher-mac.sh'\"?arch_type=$(uname -m)\")\"";
            } else {
                // Any other Linux/Unix likely to have wget installed
                cmdElement.innerText = "/bin/bash -c \"$(wget -nv -O- 'https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg/cwm-client-launcher-linux.sh'\"?arch_type=$(uname -m)\")\"";
            }

            const clipboardTrigger = sourceCodeElement.querySelector('.clipboard-trigger');
            initCodeBlock(cmdElement, clipboardTrigger)
        }

        function onAllPortsFailed() {
            removeElement(document.getElementById("state-opening-in-ide"));
            const blockEl = document.getElementById("failed-block");
            blockEl.style.display = '';
            const alternativeEl = document.getElementById('alternative-block');
            alternativeEl.style.display = ''
            if (useOldLaunchers()) {
                if (isWindows()) {
                    document.getElementById("windows-case").style.display = '';
                    const downloadLink = document.getElementById('download-client-link');
                    downloadLink.href = 'https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg/cwm-client-launcher.exe';
                    const alternativeSlot = document.getElementById('failed-windows-alternative-slot');
                    alternativeSlot.appendChild(alternativeEl);
                } else {
                    document.getElementById("linux-case").style.display = '';
                    initLinuxSourceCodeBlock();
                    const alternativeSlot = document.getElementById('failed-linux-alternative-slot');
                    alternativeSlot.appendChild(alternativeEl);
                }
            }
        }

        function suggestToDownloadGateway() {
            removeElement(document.getElementById("state-opening-in-ide"));
            const downloadGatewayEl = document.getElementById('download-gateway-block');
            downloadGatewayEl.style.display = ''
        }

        function isWindows() {
            return window.navigator.platform.indexOf('Win') > -1
        }

        function isMac() {
            return window.navigator.platform.indexOf('Mac') === 0
        }

        function download(url, filename) {
            const element = document.createElement("a");
            element.download = filename;
            element.href = url;
            element.click();
        }

        function connect(port) {
            console.info("Connecting to " + port)

            let originalRef = "https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg#p=IC&fp=39867152ABB960DC760E9F00BFEAF1E4E215998DA99D4A90E355F7742CA5B7C7";
            let connectRequest = new XMLHttpRequest();
            connectRequest.open("POST", "http://localhost:" + port + "/codeWithMe/connectThinClient", true);
            connectRequest.send(originalRef);
            removeElement(document.getElementById("state-opening-in-ide"));

            initConnectedBlock();
        }

        function initConnectedBlock() {
            const blockEl = document.getElementById("connected-block");
            blockEl.style.display = '';
            const alternativeEl = document.getElementById('alternative-block');
            alternativeEl.style.display = ''
            if (isWindows()) {
                const windowsBodyBlock = document.getElementById("connected-block-body-windows")
                windowsBodyBlock.style.display = '';
                const windowsClientBlock = document.getElementById("windows-case-client");
                const clientSlot = document.getElementById("connected-client-slot");
                clientSlot.appendChild(windowsClientBlock);
                const downloadLink = document.getElementById('download-client-link');
                downloadLink.href = 'https://code-with-me.global.jetbrains.com/8HAzzZ31KJ2SDSJzmvpFzg/cwm-client-launcher.exe';
                const alternativeSlot = document.getElementById('connected-windows-alternative-slot');
                alternativeSlot.appendChild(alternativeEl);
            } else {
                const linuxBodyBlock = document.getElementById("connected-block-body-linux")
                linuxBodyBlock.style.display = '';
                const sourceCodeElement = document.getElementById("linux-case-source-code");
                sourceCodeElement.classList.add('source-code_contrast')
                const sourceCodeSlot = document.getElementById("connected-source-code-slot");
                sourceCodeSlot.appendChild(sourceCodeElement);
                initLinuxSourceCodeBlock();
                const alternativeSlot = document.getElementById('connected-linux-alternative-slot');
                alternativeSlot.appendChild(alternativeEl);
            }
            initAccordion(blockEl.querySelector('.accordion'));
        }

        function initAccordion(el) {
            const titleEl = el.querySelector('.accordion__title');
            const bodyEl = el.querySelector('.accordion__body');
            const bodyHeight = bodyEl.offsetHeight;

            bodyEl.style.maxHeight = '0';

            const onToggle = function() {
                const isOpened = Boolean(parseInt(bodyEl.style.maxHeight, 10));
                if (isOpened) {
                    bodyEl.style.maxHeight = '0';
                    el.classList.remove('accordion_opened');
                } else {
                    bodyEl.style.maxHeight = bodyHeight + 'px';
                    el.classList.add('accordion_opened');
                }
            };

            if (titleEl) {
                titleEl.addEventListener('click', onToggle);
            }
            if (el.id) {
                const selector = '[data-accordion-target="' + el.id + '"]';
                document.addEventListener('click', function(e) {
                    const target = e.target.closest(selector);
                    if (!target) {
                        return;
                    }
                    e.preventDefault();
                    onToggle();
                })
            }
        }

        window.onload = function () {
            startup();
        };
    </script>
</head>
<body class="layout">
<main class="layout__main">
    <section class="section">
        <div class="container">
            <div class="content offset-96">
                <div class="logo">
                    <img src="/static/logos/cwm.svg" id="main-logo" alt="">
                </div>

                <h3 class="title title_h3 offset-24">
                    <span data-name>admin</span> has invited you to join their project <br/>
                    <span data-project>eshopper - security</span> in <span data-ide-title>IntelliJ IDEA Community Edition</span>.
                </h3>

                <!-- TODO Do we want to display anything here? -->
                <p class="text text_pale offset-12" style="display: none">host name: <span data-host>000000000000</span>
                </p>

                <p class="subtitle offset-24" id="state-opening-in-ide">Please wait...</p>

                <div id="connected-block" style="display: none">
                    <div class="text offset-12">
                        <p>IntelliJ IDEA Community Edition is launching JetBrains Client right now.</p>
                    </div>
                    <p class="text offset-48">
                        <a href="https://www.jetbrains.com/help/idea/code-with-me.html" class="link" target="_blank">About Code With Me</a>
                    </p>
                    <p class="text offset-12">
                        <a href="#" class="link" data-accordion-target="connected-accordion">Did anything go wrong?</a>
                    </p>
                    <div class="accordion offset-12" id="connected-accordion">
                        <div class="accordion__body">
                            <div id="connected-block-body-linux" style="display: none">
                                <p class="text">To reconnect to the project run the following command in the terminal:</p>
                                <div id="connected-source-code-slot"></div>
                                <div id="connected-linux-alternative-slot" class="offset-12"></div>
                            </div>
                            <div id="connected-block-body-windows" style="display: none">
                                <p class="text">Rerun the free JetBrains Client to reconnect to the invitation.</p>
                                <div id="connected-client-slot"></div>
                                <div id="connected-windows-alternative-slot" class="offset-24"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="text" id="toolbox-block" style="display: none">
                    <p>Connect via Toolbox App</p>
                    <a href="jetbrains://client/code-with-me/join?v=c1&build=231.9011.34&joinLink=none">
                        Open in JetBrains Toolbox
                    </a>
                    <br />
                    <a href="https://www.jetbrains.com/toolbox-app/">
                        Download JetBrains Toolbox
                    </a>
                </div>

                <div id="failed-block" style="display: none">
                    <div id="linux-case" style="display: none">
                        <p class="text offset-24">Copy the following text and run it in the terminal.</p>
                        <div class="source-code offset-12" id="linux-case-source-code">
                            <span class="source-code__copy-icon">
                                <span class="clipboard-trigger" id="clipboard-trigger">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"><path d="M9 5h10v10H9z"/><path
                                            d="M7 9H5v10h10v-2H7V9z"/></svg>
                                </span>
                            </span>
                            <pre class="source-code__content"><code id="cmd-placeholder"></code></pre>
                        </div>
                        <div id="failed-linux-alternative-slot" class="offset-12"></div>
                    </div>

                    <div id="windows-case" style="display: none">
                        <p class="text offset-24">
                            To access the invitation, download and run the free JetBrains Client.
                        </p>

                        <div class="client" id="windows-case-client">
                            <div class="client__button offset-24">
                                <a href="#" id="download-client-link" class="button">Download</a>
                            </div>
                            <div class="client__desc offset-24">
                                <p class="text text_small">
                                    <span data-client-title>JetBrains Client</span>, <span data-thin-version>231.9011.34</span>
                                </p>
                            </div>
                        </div>
                        <div id="failed-windows-alternative-slot" class="offset-24"></div>
                    </div>
                    <p class="text offset-24"><a href="https://www.jetbrains.com/help/idea/code-with-me.html" class="link">About Code With Me</a></p>
                </div>

                <div class="text" id="alternative-block" style="display: none">
                    <p>Alternatively you can launch <span data-ide-title>IntelliJ IDEA Community Edition</span> version <span data-idea-version-title>2020.2</span> or newer with Code With Me plugin.</p>
                    <p>You can download the latest version of <span data-ide-title>IntelliJ IDEA Community Edition</span> <a href="https://www.jetbrains.com/idea/download/" class="link">here</a>.</p>
                </div>

                <div class="text" id="download-gateway-block" style="display: none">
                    <p>Download JetBrains Gateway via Toolbox App</p>
                </div>
            </div>
        </div>
    </section>
</main>
<footer class="layout__footer">
    <div class="container">
        <p class="copyright">Copyright © 2000-2023 JetBrains s.r.o.</p>
    </div>
</footer>
</body>
</html>
