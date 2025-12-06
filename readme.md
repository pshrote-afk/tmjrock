# TMJRock Library

A lightweight JavaScript library inspired by jQuery, providing simplified DOM manipulation, AJAX functionality, modals, accordions, and data grids with pagination.

## Installation

Include the library files in your HTML:

```html
<link rel='stylesheet' href='tmjrock.css' />
<script src='tmjrock.js'></script>
```

## Core Functions

### TMJRockElement Class

When you select an element using `$$$(elementId)`, it returns a `TMJRockElement` instance that wraps the native DOM element and provides helper methods.

**Properties:**
- `.element` - Access the underlying native DOM element
```javascript
var tmjElement = $$$("myDiv");
var nativeElement = tmjElement.element;
```

The `TMJRockElement` class provides the following properties and methods:

**Properties:**
- `.element` - Returns the underlying native DOM element

**Methods:**

The TMJRockElement instance provides the following methods:
- `.html(content)` - Get or set innerHTML (documented below)
- `.value(content)` - Get or set value (documented below)  
- `.fillComboBox(configurations)` - Populate select dropdowns (documented below)
### DOM Selection and Manipulation

**`$$$(elementId)`** - Select an element by ID and return a TMJRockElement wrapper

```javascript
var element = $$$("myElement");
```

**`.html(content)`** - Get or set innerHTML
```javascript
$$$("myDiv").html("New content");
var content = $$$("myDiv").html();
```

**`.value(content)`** - Get or set value property
```javascript
$$$("myInput").value("New value");
var value = $$$("myInput").value();
```

**`.fillComboBox(configurations)`** - Populate a select dropdown
```javascript
$$$("designationCode").fillComboBox({
    "dataSource": arrayOfObjects,
    "text": "displayField",
    "value": "valueField",
    "firstOption": {
        "text": "<Select Option>",
        "value": "-1"
    }
});
```

### AJAX

**`$$$.ajax(jsonObject)`** - Make asynchronous HTTP requests

**GET Request:**
```javascript
$$$.ajax({
    "url": "/api/endpoint",
    "methodType": "GET",
    "data": { "param1": "value1" },  // Optional
    "success": function(responseData) {
        // Handle success
    },
    "failure": function() {
        // Handle failure
    }
});
```

**POST Request:**
```javascript
$$$.ajax({
    "methodType": "POST",
    "url": "/api/endpoint",
    "data": { "key": "value" },
    "sendJSON": false,  // false = form-encoded, true = JSON
    "success": function(responseData) {
        // Handle success
    },
    "failure": function() {
        // Handle failure
    }
});
```

### Modals

Create modal dialogs with customizable headers, footers, and lifecycle hooks.

**HTML Structure:**
```html
<div id='myModal' 
     closeButton="true" 
     forModal="true" 
     style='display:none' 
     size="400x300" 
     header="Modal Title"
     footer="Footer Text" 
     maskColor="#3355ff" 
     modalBackgroundColor="#549933"
     beforeOpening="beforeOpenFunction()"
     afterOpening="afterOpenFunction()"
     beforeClosing="beforeCloseFunction()"
     afterClosing="afterCloseFunction()">
    Modal content here
</div>
```

**Show Modal:**
```javascript
$$$.modals.show("myModal");
```

**Modal Attributes:**
- `forModal="true"` - Required to mark element as modal
- `closeButton="true"` - Add close button (X)
- `size="widthxheight"` - Set dimensions (e.g., "400x300")
- `header` - Header text
- `footer` - Footer text
- `maskColor` - Background mask color
- `modalBackgroundColor` - Modal background color
- `beforeOpening` - Function called before opening (return false to cancel)
- `afterOpening` - Function called after opening
- `beforeClosing` - Function called before closing (return false to cancel)
- `afterClosing` - Function called after closing

### Accordions

Create collapsible accordion panels automatically.

**HTML Structure:**
```html
<div accordian="true">
    <h3>Heading 1</h3>
    <div>Content 1</div>
    
    <h3>Heading 2</h3>
    <div>Content 2</div>
    
    <h3>Heading 3</h3>
    <div>Content 3</div>
</div>
```

Just add `accordian="true"` to a container with alternating `<h3>` headings and `<div>` content blocks. The library automatically makes them collapsible.

### Data Grid with Pagination

**`new Grid(dataTableId, paginationTableId, dataSource, columns, pageSize, numberOfPaginationControls)`**

Create paginated data tables.

**HTML Structure:**
```html
<div class='tmjrock_tmgrid_header_division'>
    <table class='tmjrock_tmgrid_head'>
        <tr>
            <td>S.No.</td>
            <td>Column 1</td>
            <td>Column 2</td>
        </tr>
    </table>
</div>

<div class='tmjrock_tmgrid_body_division'>
    <table class='tmjrock_tmgrid_body' id='dataTable'></table>
</div>

<div class='tmjrock_tmgrid_pagination_division'>
    <table class='tmjrock_tmgrid_pagination' id='dataTablePagination'></table>
</div>
```

**JavaScript:**
```javascript
var students = [
    { roll: "101", name: "John", mother: "Jane", father: "Jack" },
    { roll: "102", name: "Alice", mother: "Amy", father: "Andrew" }
];

var columns = [
    { field: "roll" },
    { field: "name" },
    { field: "mother" },
    { field: "father" }
];

$$$.model.grid = new Grid('dataTable', 'dataTablePagination', students, columns, 20, 5);
```

**Parameters:**
- `dataTableId` - ID of body table
- `paginationTableId` - ID of pagination table
- `dataSource` - Array of data objects
- `columns` - Array of column definitions with `field` property
- `pageSize` - Number of rows per page
- `numberOfPaginationControls` - Number of page buttons to show

### Initialization Hooks

**`$$$.onDocumentLoaded(function)`** - Execute function after document loads

```javascript
$$$.onDocumentLoaded(function() {
    // Initialize your components
});
```

## Internal Model

**`$$$.model`** - Framework state object containing:
- `onStartup` - Array of functions to run on load (functions passed to $$$.onDocumentLoaded() are added to this array)
- `accordians` - Array of accordion instances
- `modals` - Array of modal instances
- `grid` - Current grid instance

## CSS Classes

The library includes predefined styles for:
- `.tmjrock_modalMask` - Modal overlay
- `.tmjrock_modalMaskDivision` - Modal container
- `.tmjrock_closeButton` - Modal close button
- `.tmjrock_tmgrid_*` - Grid styling classes
- Grid header, body, and pagination divisions

## Event Handling

The library automatically:
- Initializes accordions on page load
- Loads modals on page load
- Synchronizes grid header/body horizontal scrolling
- Manages modal lifecycle events

## Examples

See the included example files:
- `accordian_example.html` - Accordion implementation
- `modal_example.html` - Modal dialogs
- `grid_with_pagination_example.html` - Data grid
- `ajax_get_post_examples.html` - AJAX requests
- `example_combining_all_features.html` - All features combined

