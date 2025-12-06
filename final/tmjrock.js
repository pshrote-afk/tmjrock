        class Grid {
            constructor(dataTableId, paginationTableId, dataSource, columns, pageSize) {
                var objectAddress = this;
                this.dataTableId = dataTableId;
                this.paginationTableId = paginationTableId;
                this.dataSource = dataSource;
		this.columns = columns;
                this.pageSize = pageSize;

                this.pageNumber = 1;
                this.numberOfPaginationControls = 5;

                this.update();
                this.updatePagination();
            }

            update() {
                var dataTable = document.getElementById(this.dataTableId);

                //first remove old entries if any
                while (dataTable.rows.length != 0) dataTable.deleteRow(0);

                //add new entries
                var startFromIndex = (this.pageNumber - 1) * this.pageSize;
                var endAtIndex = startFromIndex + this.pageSize - 1;
                if (endAtIndex >= this.dataSource.length) endAtIndex = this.dataSource.length - 1;	//length is 1 based, endAtIndex is 0 based

                var tr;
                var td;
		var fieldName;
                for (var i = startFromIndex; i <= endAtIndex; i++) {
                    tr = document.createElement("tr");
                    td = document.createElement("td");

		    //sno
                    td.innerHTML = i + 1;
                    tr.appendChild(td);

  		    for(var j = 0; j < this.columns.length; j++) {
			td = document.createElement("td");
			fieldName = this.columns[j].field;
			td.innerHTML = this.dataSource[i][fieldName];
			tr.appendChild(td);
		    }
              
                    dataTable.appendChild(tr);
                }


            }
            updatePagination() {
                function createPageChangeFunction(objectAddress, newPageNumber) {
                    return function () {
                        objectAddress.setGridPageNumber(newPageNumber);
                    };
                }

                var paginationTable = document.getElementById(this.paginationTableId);


                //first remove old entries if any
                var rows = paginationTable.querySelectorAll("tr");	// returns a static NodeList		//var rows = paginationTable.getElementsByTagName("tr");    returns a live-HTML Collection object
                var row;
                for (var k = 0; k < rows.length; k++) {
                    row = rows[k];
                    row.remove();
                }

                //add new entries
                let startFrom = Math.floor((this.pageNumber - 1) / this.numberOfPaginationControls) * this.numberOfPaginationControls + 1;
                let endAt = startFrom + this.numberOfPaginationControls - 1;

                var totalNumberOfPages = Math.floor(this.dataSource.length / this.pageSize);
                if (this.dataSource.length % this.pageSize != 0) totalNumberOfPages++;
                if (endAt > totalNumberOfPages) endAt = totalNumberOfPages;


                var tr;
                var td;
                var anchor;
                tr = document.createElement("tr");
                paginationTable.appendChild(tr);

                if (startFrom > 1) {
                    td = document.createElement("td");
                    anchor = document.createElement("a");
                    anchor.onclick = createPageChangeFunction(this, startFrom - 1);
                    anchor.href = 'javascript:void(0)';
                    anchor.text = "prev";
                    td.appendChild(anchor);
                    tr.appendChild(td);
                }

                for (let i = startFrom; i <= endAt; i++) {
                    td = document.createElement("td");
                    if (this.pageNumber == i) {
                        td.innerHTML = "<b>" + i + "</b>";
                    }
                    else {
                        anchor = document.createElement("a");
                        anchor.onclick = createPageChangeFunction(this, i);
                        anchor.href = 'javascript:void(0)';
                        anchor.text = i;
                        td.appendChild(anchor);
                    }
                    tr.appendChild(td);
                }

                if (endAt < totalNumberOfPages) {
                    td = document.createElement("td");
                    anchor = document.createElement("a");
                    anchor.onclick = createPageChangeFunction(this, endAt + 1);
                    anchor.href = 'javascript:void(0)';
                    anchor.text = "next";
                    td.appendChild(anchor);
                    tr.appendChild(td);
                }

            } //end of updatePagination()

            setGridPageNumber(newPageNumber) {
                this.pageNumber = newPageNumber;
                this.update();
                this.updatePagination();
                return false;		//returning false for function called by 'onclick' ensures that request is not sent for attribute of 'href'
            }

        } //end of Grid


        $$$.gridLoader = function () {
            var tmjrock_tmgrid_header_division = document.querySelector(".tmjrock_tmgrid_header_division");
            var tmjrock_tmgrid_body_division = document.querySelector(".tmjrock_tmgrid_body_division");
			if(tmjrock_tmgrid_header_division!=null && tmjrock_tmgrid_body_division!=null)
			{
				tmjrock_tmgrid_body_division.addEventListener('scroll', function () {
					tmjrock_tmgrid_header_division.scrollLeft = tmjrock_tmgrid_body_division.scrollLeft;
				});
			}
			// initialize dataset
	
        	// let pageSize = 20;
        	// model.grid = new Grid('dataTable', 'dataTablePagination', dataSource, pageSize);

	
		}


		//TMJRock accordian section starts
		function $$$(cid) {
			let element = document.getElementById(cid);
			if (!element) throw "Invalid id: " + cid;
			return new TMJRockElement(element);
		} //end of $$$ function

		$$$.model = {
			"onStartup": [],
			"accordians": [],
			"modals": [],
			"grid": null
		};

		//modal specific code starts here

		$$$.modals = {};	//just an empty object - to be able to do $$$.modals.show("ab");
		/*above is same as
		var $$$ = {
		"modals":{}
		};
		*/

		$$$.modals.show = function (mid)		//mid = modal id
		{
			var modal = null;
			for (var i = 0; i < $$$.model.modals.length; i++) {
				if ($$$.model.modals[i].getContentId() == mid) {
					modal = $$$.model.modals[i];
					break;
				}
			}
			if (modal == null) return;
			modal.show();
		}

		//start of Modal class
		//foll. is a class
		function Modal(cref)	//cref = content reference
		{
			var objectAddress = this;
			this.afterOpening = null;
			this.afterClosing = null;
			var contentReference = cref;

			this.getContentId = function () {
				return contentReference.id;
			};


			var modalMask = document.createElement("div");
			modalMask.classList.add("tmjrock_modalMask");
			modalMask.style.display = 'none';
			var modalMaskDivision = document.createElement("div");
			modalMaskDivision.classList.add("tmjrock_modalMaskDivision");
			modalMaskDivision.style.display = 'none';

			document.body.appendChild(modalMask);
			document.body.appendChild(modalMaskDivision);

			var headerDivision = document.createElement("div");
			headerDivision.style.background = "red";
			headerDivision.style.right = "0";
			headerDivision.style.height = "40px";
			headerDivision.style.padding = "5px";
			modalMaskDivision.appendChild(headerDivision);

			if (contentReference.hasAttribute("size")) {
				var sz = contentReference.getAttribute("size");
				dimensions = sz.split("x");
				if (dimensions.length != 2) throw "In case of modal, specify size as 'widthxheight' only";
				modalMaskDivision.style.width = dimensions[0] + "px";
				modalMaskDivision.style.height = dimensions[1] + "px";
			}
			else {
				modalMaskDivision.style.width = "300px";
				modalMaskDivision.style.height = "300px";
			}
			if (contentReference.hasAttribute("header")) {
				var hd = contentReference.getAttribute("header");
				headerDivision.innerHTML = hd;
			}

			if (contentReference.hasAttribute("maskColor")) {
				var mkc = contentReference.getAttribute("maskColor");
				modalMaskDivision.style.background = mkc;
			}
			if (contentReference.hasAttribute("modalBackgroundColor")) {
				var mbc = contentReference.getAttribute("modalBackgroundColor");
				modalMaskDivision.style.background = mbc;
			}

			var contentDivision = document.createElement("div");
			contentDivision.style.height = (modalMaskDivision.style.height.substring(0, modalMaskDivision.style.height.length - 2) - 130) + "px";
			contentDivision.style.width = "98%";
			contentDivision.style.overflow = "auto";
			contentDivision.style.padding = "5px";
			contentReference.remove();
			contentDivision.appendChild(contentReference);
			contentReference.style.display = "block";
			contentReference.style.visibility = "visible";
			modalMaskDivision.appendChild(contentDivision);

			var footerDivision = document.createElement("div");
			footerDivision.style.background = "pink";
			footerDivision.style.left = "0";
			footerDivision.style.right = "0";
			footerDivision.style.height = "40px";
			footerDivision.style.position = "absolute";
			footerDivision.style.bottom = "0";
			footerDivision.style.padding = "5px";
			modalMaskDivision.appendChild(footerDivision);

			if (contentReference.hasAttribute("footer")) {
				var ft = contentReference.getAttribute("footer");
				footerDivision.innerHTML = ft;
			}

			var closeButtonSpan = null;
			if (contentReference.hasAttribute("closeButton")) {
				var cb = contentReference.getAttribute("closeButton");
				if (cb.toUpperCase() == "TRUE") {
					closeButtonSpan = document.createElement("span");
					closeButtonSpan.classList.add("tmjrock_closeButton");
					var closeButtonMarker = document.createTextNode("x");
					closeButtonSpan.appendChild(closeButtonMarker);
					headerDivision.appendChild(closeButtonSpan);
				}
			}

			if (contentReference.hasAttribute("beforeOpening")) {
				var oo = contentReference.getAttribute("beforeOpening");
				this.beforeOpening = oo;
			}
			if (contentReference.hasAttribute("afterOpening")) {
				var oo = contentReference.getAttribute("afterOpening");
				this.afterOpening = oo;
			}
			if (contentReference.hasAttribute("beforeClosing")) {
				var oc = contentReference.getAttribute("beforeClosing");
				this.beforeClosing = oc;
			}
			if (contentReference.hasAttribute("afterClosing")) {
				var oc = contentReference.getAttribute("afterClosing");
				this.afterClosing = oc;
			}
			//lect 75 end
			this.show = function () {
				let openModal = true;	//true by default
				if (this.beforeOpening) {
					openModal = eval(this.beforeOpening);
				}
				if (openModal) {
					modalMask.style.display = 'block';
					modalMaskDivision.style.display = 'block';
					if (this.afterOpening) setTimeout(function () { eval(objectAddress.afterOpening); }, 100);
				}
			};

			if (closeButtonSpan != null) {
				closeButtonSpan.onclick = function () {
					let closeModal = true;
					if (objectAddress.beforeClosing) {
						closeModal = eval(objectAddress.beforeClosing);
					}
					if (closeModal) {
						modalMask.style.display = "none";
						modalMaskDivision.style.display = "none";
						if (objectAddress.afterClosing) setTimeout(function () { eval(objectAddress.afterClosing); }, 100);
					}
				};
			}
		}
		//end of Modal class

		//modal specific code ends here

		$$$.accordianHeadingClicked = function (accordianIndex, panelIndex) {
			if ($$$.model.accordians[accordianIndex].expandedIndex != -1) $$$.model.accordians[accordianIndex].panels[$$$.model.accordians[accordianIndex].expandedIndex].style.display = 'none';
			$$$.model.accordians[accordianIndex].panels[panelIndex + 1].style.display = $$$.model.accordians[accordianIndex].panels[panelIndex + 1].oldDisplay;
			$$$.model.accordians[accordianIndex].expandedIndex = panelIndex + 1;
		}

		$$$.toAccordian = function (accord) {
			let panels = [];
			let expandedIndex = -1;
			let children = accord.childNodes;

			for (let x = 0; x < children.length; x++)	// loop also throws children of type '#text'
			{
				if (children[x].nodeName == "H3") {
					panels[panels.length] = children[x];
				}
				if (children[x].nodeName == "DIV") {
					panels[panels.length] = children[x];
				}
			}
			if (panels.length % 2 != 0) throw "Headings and division malformed to create accordian";
			for (x = 0; x < panels.length; x += 2) {
				if (panels[x].nodeName != "H3") throw "Headings and division malformed to create accordian";
				if (panels[x + 1].nodeName != "DIV") throw "Headings and division malformed to create accordian";
			}

			function createClickHandler(accordianIndex, panelIndex) {
				return function () {
					$$$.accordianHeadingClicked(accordianIndex, panelIndex);
				};
			}

			let accordianIndex = $$$.model.accordians.length;

			for (x = 0; x < panels.length; x += 2) {
				panels[x].onclick = createClickHandler(accordianIndex, x);
				panels[x + 1].oldDisplay = panels[x + 1].style.display;
				panels[x + 1].style.display = "none"
			}

			$$$.model.accordians[accordianIndex] = {
				"panels": panels,
				"expandedIndex": -1
			} //end of anonymous function

		} //end of $$$.toAccordian

		$$$.onDocumentLoaded = function (func) {
			if ((typeof func) != "function") throw "Expected function; found " + (typeof func) + " in call to onDocumentLoaded";
			$$$.model.onStartup[$$$.model.onStartup.length] = func;
		}

		$$$.initFramework = function () {
			let allTags = document.getElementsByTagName("*");
			let t = null;
			let i = 0;
			let a = null;
			for (i = 0; i < allTags.length; i++) {
				t = allTags[i];
				if (t.hasAttribute("accordian")) {
					a = t.getAttribute("accordian");
					if (a == "true") {
						$$$.toAccordian(t);
					}
				}
			}
			let x = 0;
			while (x < $$$.model.onStartup.length)   // onStartup is empty after we introduced accordian="true" concept
			{
				$$$.model.onStartup[x]();
				x++;
			}
		}

		$$$.modalLoader = function () {
			var allTags = document.getElementsByTagName("*");	//returns a 'live collection' of 'references'
			var tag;
			var forModal;	// boolean
			var forModalTags = [];
			for (var i = 0; i < allTags.length; i++) {
				tag = allTags[i];
				forModal = tag.getAttribute("forModal");
				if (forModal != undefined && forModal.toUpperCase() == "TRUE") {
					forModalTags.push(tag);			//new Modal("abc") removes abc from DOM. Hence DOM indexing changes. Hence it is likely that some elements might get be skipped.
				}					//Hence collect tags with attributes forModal="true" in an array named 'forModalTags' first.
			}

			for (var i = 0; i < forModalTags.length; i++) {
				tag = forModalTags[i];
				$$$.model.modals[$$$.model.modals.length] = new Modal(tag);		//process each tag. new Modal() removes that tag from DOM.
			}

		}//end of modalLoader()

		function TMJRockElement(element) {
			this.element = element;

			this.fillComboBox = function (configurations) {
				//to do: apply validation for fields recevied. Lect 66.

				var designations = configurations.dataSource;

				var obj1;
				if (configurations.firstOption != null) {
					obj1 = document.createElement("option");
					obj1.value = configurations.firstOption.value;
					obj1.text = configurations.firstOption.text;
					element.appendChild(obj1);
				}
				for (var i = 0; i < designations.length; i++) {
					obj1 = document.createElement("option");
					obj1.value = designations[i][configurations.value]; // In Java we would've written - designations[i].(configurations.value);
					obj1.text = designations[i][configurations.text];
					element.appendChild(obj1);
				}
			}; //end of TMJRockElement fillComboBox().

			this.html = function (content) {
				if (typeof this.element.innerHTML == "string") {
					if ((typeof content) == "string") {
						this.element.innerHTML = content;
					}
					return this.element.innerHTML;
				}
				return null;
			}

			this.value = function (content) {
				if (typeof this.element.value) {
					if ((typeof content) == "string") {
						this.element.value = content;
					}
					return this.element.value;
				}
				return null;
			}


		} //end of TMJRockElement

		//start of ajax
		$$$.ajax = function (jsonObject) {
			if (!jsonObject["url"]) throw "url property is missing in call to ajax";
			let url = jsonObject["url"];
			if ((typeof url) != "string") throw "url property should be of string type in call to ajax";

			let methodType = "GET"
			if (jsonObject["methodType"]) {
				methodType = jsonObject["methodType"];
				if ((typeof methodType) != "string") throw "methodType property should be of string type in call to ajax";
				methodType = methodType.toUpperCase();
				if (["GET", "POST"].includes(methodType) == false) throw "methodType should be GET/POST in call to ajax";
			}

			let onSuccess = null;
			if (jsonObject["success"]) {
				onSuccess = jsonObject["success"];
				if ((typeof onSuccess) != "function") throw "success property should be a function in call to ajax";
			}

			let onFailure = null;
			if (jsonObject["failure"]) {
				onFailure = jsonObject["failure"];
				if ((typeof onFailure) != "function") throw "failure property should be a function in call to ajax";
			}

			if (methodType == "GET") {
				var xmlHttpRequest = new XMLHttpRequest();
				xmlHttpRequest.onreadystatechange = function () {
					if (this.readyState == 4) {
						if (this.status == 200) {
							var responseData = this.responseText;
							if (onSuccess) onSuccess(responseData);
						}
						else {
							if (onFailure) onFailure();
						}
					}
				};

				if (jsonObject["data"]) {
					let jsonData = jsonObject["data"];
					let queryString = "";
					let qsName;
					let qsValue;
					let xx = 0;
					for (k in jsonData) {
						if (xx == 0) queryString = "?";	//Will run first time
						if (xx > 0) queryString += "&";	//Won't run first time. Then will run everytime.
						xx++;
						qsName = encodeURI(k);
						qsValue = encodeURI(jsonData[k]);
						queryString = queryString + qsName + "=" + qsValue;
					}
					url += queryString;
				}
				xmlHttpRequest.open(methodType, url, true);
				xmlHttpRequest.send();
			} //"GET" part ends here

			if (methodType == "POST") {
				var xmlHttpRequest = new XMLHttpRequest();
				xmlHttpRequest.onreadystatechange = function () {
					if (this.readyState == 4) {
						if (this.status == 200) {
							var responseData = this.responseText;
							if (onSuccess) onSuccess(responseData);
						}
						else {
							if (onFailure) onFailure();
						}
					}
				};


				let jsonData = {};
				if (jsonObject["data"]) {
					jsonData = jsonObject["data"];
				}

				if ((typeof jsonObject["sendJSON"]) !== "boolean") throw "sendJSON property should be of boolean type in call to ajax";

				if (jsonObject["sendJSON"] == true) {
					xmlHttpRequest.open("POST", url, true);
					xmlHttpRequest.setRequestHeader("Content-Type", "application/json");
					xmlHttpRequest.send(JSON.stringify(jsonData));
				}
				else if (jsonObject["sendJSON"] == false || jsonObject["sendJSON"] == undefined) {
					//make queryString
					var queryString = "";
					var qsName;
					var qsValue;
					var xx = 0;
					for (k in jsonData) {
						if (xx > 0) queryString += "&";
						xx++;
						qsName = encodeURI(k);
						qsValue = encodeURI(jsonData[k]);
						queryString = queryString + qsName + "=" + qsValue;
					}
				}
				else {
					throw "some problem in sendJSON property";
				}

				xmlHttpRequest.open("POST", url, true);
				xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				xmlHttpRequest.send(queryString);
			}

		}//end of $$$.ajax function

		//TMJRock accordian section ends

		window.addEventListener('load', function () {
			$$$.initFramework();	//initializes accordian panes
			$$$.modalLoader();	//initializes modals
			$$$.gridLoader();	//makes header and body tables scroll together horizontally
		});

		//TMJRock ends

