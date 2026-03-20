package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataVisualiser
{
  //HORIZONTAL Bar chart instead of vertical
  public String renderBarChart(Map<String, Integer> buckets, String title)
  {
    int peak         = Collections.max(buckets.values());
    int slotCount    = buckets.size();
    int rowHeight    = 40;
    int barPadding   = 7;
    int marginLeft   = 100;
    int marginRight  = 70;
    int marginTop    = 50;
    int marginBottom = 20;
    int canvasWidth  = 750;
    int canvasHeight = marginTop + slotCount * rowHeight + marginBottom;
    int plotWidth    = canvasWidth - marginLeft - marginRight;

    StringBuilder svg = new StringBuilder();
    svg.append(String.format(
      "<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d' style='background:#0d0d0d;border-radius:8px;'>",
      canvasWidth, canvasHeight
    ));

    svg.append(String.format(
      "<text x='%d' y='28' text-anchor='middle' font-size='16' font-family='monospace' font-weight='bold' fill='#cc99ff'>%s</text>",
      canvasWidth / 2, title
    ));

    svg.append(String.format(
      "<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='#6600cc' stroke-width='2'/>",
      marginLeft, marginTop, marginLeft, marginTop + slotCount * rowHeight
    ));

    int slot = 0;
    for (Map.Entry<String, Integer> entry : buckets.entrySet())
    {
      String bracket  = entry.getKey();
      int    tally    = entry.getValue();
      int    barWidth = peak > 0 ? (int) ((double) tally / peak * plotWidth) : 0;
      int    barY     = marginTop + slot * rowHeight + barPadding;
      int    barH     = rowHeight - barPadding * 2;

      svg.append(String.format(
        "<text x='%d' y='%d' text-anchor='end' font-size='13' font-family='monospace' fill='#cc99ff'>%s</text>",
        marginLeft - 8, barY + barH / 2 + 5, bracket
      ));

      svg.append(String.format(
        "<rect x='%d' y='%d' width='%d' height='%d' fill='#7b2fbe' rx='3'/>",
        marginLeft, barY, barWidth, barH
      ));

      if (tally > 0)
      {
        svg.append(String.format(
          "<text x='%d' y='%d' font-size='13' font-family='monospace' fill='#ffffff'>%d</text>",
          marginLeft + barWidth + 6, barY + barH / 2 + 5, tally
        ));
      }

      slot++;
    }

    svg.append("</svg>");
    return svg.toString();
  }

  public String renderPieChart(Map<String, Integer> segments, String title)
  {
    int cx = 240, cy = 230, radius = 170;
    int totalWidth = 620, totalHeight = 500;

    String[] palette = {"#7b2fbe", "#2563eb", "#a855f7", "#1d4ed8", "#6d28d9"};

    int grandTotal = segments.values().stream().mapToInt(Integer::intValue).sum();

    StringBuilder svg = new StringBuilder();
    svg.append(String.format(
      "<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d' style='background:#0d0d0d;border-radius:8px;'>",
      totalWidth, totalHeight
    ));

    svg.append(String.format(
      "<text x='%d' y='28' text-anchor='middle' font-size='16' font-family='monospace' font-weight='bold' fill='#cc99ff'>%s</text>",
      cx, title
    ));

    double sweepStart = -Math.PI / 2;
    int colourIndex = 0;
    List<String> legendEntries = new ArrayList<>();

    for (Map.Entry<String, Integer> entry : segments.entrySet())
    {
      String label      = entry.getKey();
      int    tally      = entry.getValue();
      double portion    = (double) tally / grandTotal;
      double sweepAngle = portion * 2 * Math.PI;
      double sweepEnd   = sweepStart + sweepAngle;

      double x1 = cx + radius * Math.cos(sweepStart);
      double y1 = cy + radius * Math.sin(sweepStart);
      double x2 = cx + radius * Math.cos(sweepEnd);
      double y2 = cy + radius * Math.sin(sweepEnd);

      int    largeArc = sweepAngle > Math.PI ? 1 : 0;
      String colour   = palette[colourIndex % palette.length];

      svg.append(String.format(
        "<path d='M %d %d L %.2f %.2f A %d %d 0 %d 1 %.2f %.2f Z' fill='%s'/>",
        cx, cy, x1, y1, radius, radius, largeArc, x2, y2, colour
      ));

      double midAngle = sweepStart + sweepAngle / 2;
      double labelR   = radius * 0.62;
      double labelX   = cx + labelR * Math.cos(midAngle);
      double labelY   = cy + labelR * Math.sin(midAngle);
      svg.append(String.format(
        "<text x='%.1f' y='%.1f' text-anchor='middle' font-size='13' font-family='monospace' font-weight='bold' fill='white'>%.1f%%</text>",
        labelX, labelY + 5, portion * 100
      ));

      legendEntries.add(String.format(
        "<rect x='%d' y='%d' width='16' height='16' fill='%s' rx='2'/>" +
        "<text x='%d' y='%d' font-size='13' font-family='monospace' fill='#cc99ff'>%s (%d)</text>",
        totalWidth - 190, 50 + colourIndex * 28, colour,
        totalWidth - 168, 63 + colourIndex * 28, label, tally
      ));

      sweepStart = sweepEnd;
      colourIndex++;
    }

    for (String entry : legendEntries) svg.append(entry);
    svg.append("</svg>");
    return svg.toString();
  }
}