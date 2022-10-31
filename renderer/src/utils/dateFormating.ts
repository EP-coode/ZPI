export function formatDate(date: Date): string {
  return date.toLocaleDateString("pl-PL", {
    weekday: "long",
    year: "numeric",
    month: "numeric",
    day: "numeric",
  });
}
