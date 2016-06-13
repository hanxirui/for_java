// Call this from the developer console and you can control both instances
var calendars = {};

$(document).ready( function() {
   
    // Assuming you've got the appropriate language files,
    // clndr will respect whatever moment's language is set to.
    // moment.locale('ru');

    // Here's some magic to make sure the dates are happening this month.
    var thisMonth = moment().format('YYYY-MM');
    currMonth = thisMonth;
    // Events to load into calendar
    var eventArray = new Array();
    
    
    
//    [
//     {
//         title: 'Multi-Day Event',
//         date: thisMonth + '-21'
//     }, {
//         date: thisMonth + '-17',
//         title: 'Another Multi-Day Event'
//     }, {
//         date: thisMonth + '-27',
//         title: 'Single Day Event'
//     }
// ]
    var initCalendar = function(){
    	
        // The order of the click handlers is predictable. Direct click action
        // callbacks come first: click, nextMonth, previousMonth, nextYear,
        // previousYear, nextInterval, previousInterval, or today. Then
        // onMonthChange (if the month changed), inIntervalChange if the interval
        // has changed, and finally onYearChange (if the year changed).
        calendars.clndr1 = $('#canlender').clndr({
            events: eventArray,
            clickEvents: {
                click: function (target) {
                    console.log('Cal-1 clicked: ', target.date._i);
                   
                    $.getJSON(ctx+"getEventDay.do?day="+target.date._i, null, function (result) {
                    	
                		
                		if(result.data.length>0){
                			bizEdit(result.data[0]);
                		}else{
                			$("#riqi").val(target.date._i);
                			bizAdd();
                		}
                	});
                    
                  
                	 $("#riqi").val(target.date._i);
                },
                today: function () {
                    console.log('Cal-1 today');
                },
                nextMonth: function () {
                    console.log('Cal-1 next month');
                },
                previousMonth: function () {
                    console.log('Cal-1 previous month');
                },
                onMonthChange: function (month) {
                    console.log('Cal-1 month changed');
                    console.log(month.format('YYYY-MM'));
                    getEventMonth(month.format('YYYY-MM'));
                    currMonth = month.format('YYYY-MM');
                },
                nextYear: function () {
                    console.log('Cal-1 next year');
                },
                previousYear: function () {
                    console.log('Cal-1 previous year');
                },
                onYearChange: function () {
                    console.log('Cal-1 year changed');
                },
                nextInterval: function () {
                    console.log('Cal-1 next interval');
                },
                previousInterval: function () {
                    console.log('Cal-1 previous interval');
                },
                onIntervalChange: function () {
                    console.log('Cal-1 interval changed');
                }
            },
            multiDayEvents: {
                singleDay: 'date',
                endDate: 'endDate',
                startDate: 'startDate'
            },
            showAdjacentMonths: true,
            adjacentDaysChangeMonth: false
        });

    }

    
    var getEventMonth = function(month){
	    	$.getJSON(ctx+"getEventMonth.do?month="+month, null, function (result) {
	    	   try{
		    		$.each( result.data, function(i, n){
		    			 var event = {};
		    			 event.title = n.bizplatTitle;
		    			 event.date = n.riqi;
		    			 eventArray[i] = event;
		    		});
    		   }finally{
    	    	   initCalendar();
    	       }
	    	});
    }
    
    getEventMonth(thisMonth);
    

   

    // Bind all clndrs to the left and right arrow keys
    $(document).keydown( function(e) {
        // Left arrow
        if (e.keyCode == 37) {
            calendars.clndr1.back();
           
        }

        // Right arrow
        if (e.keyCode == 39) {
            calendars.clndr1.forward();
           
        }
    });
});