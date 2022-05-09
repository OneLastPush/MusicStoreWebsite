package com.trouble.paginator;

import com.trouble.entities.Reviews;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author Frank Birikundavyi
 */
public class ReviewPaginator implements Serializable
{

    public static final int DEFAULT_RECORDS_NUMBER = 30;
    public static final int DEFAULT_PAGE_INDEX = 1;

    private int records;
    private int recordsTotal;
    private int pageIndex;
    private int pages;
    private List<Reviews> list;
    private List<Reviews> origList;

    public ReviewPaginator(List<Reviews> origList)
    {
        this.origList = origList;
        this.records = DEFAULT_RECORDS_NUMBER;
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.recordsTotal = origList.size();

        if (records > 0)
        {
            //Number of page in total 
            if (records <= 0)
            {
                pages = 1;
            }
            else
            {
                pages = recordsTotal / records;
            }
            //Add an extra page if there is leftover entries
            if (recordsTotal % records > 0)
            {
                pages++;
            }
            if (pages == 0)
            {
                pages = 1;
            }
        }
        else
        {
            records = 1;
            pages = 1;
        }
        updateModel();
    }

    private void updateModel()
    {
        int fromIndex = getFirst();
        int toIndex = getFirst() + records;
        if (toIndex > this.recordsTotal)
        {
            toIndex = this.recordsTotal;
        }
        this.list = origList.subList(fromIndex, toIndex);
    }

    public int getFirst()
    {
        return (pageIndex * records) - records;
    }

    public void next()
    {
        if (this.pageIndex < pages)
        {
            this.pageIndex++;
        }

        updateModel();
    }

    public void prev()
    {
        if (this.pageIndex > 1)
        {
            this.pageIndex--;
        }

        updateModel();
    }

    public int getRecords()
    {
        return records;
    }

    public int getRecordsTotal()
    {
        return recordsTotal;
    }

    public int getPageIndex()
    {
        return pageIndex;
    }

    public int getPages()
    {
        return pages;
    }

    public List<Reviews> getList()
    {
        return list;
    }

    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }
}
