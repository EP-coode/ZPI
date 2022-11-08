export function formatDate(date: string): string | null {
  try {
    const _date = new Date(date);
    return _date.toLocaleDateString("pl-PL", {
      weekday: "long",
      year: "numeric",
      month: "numeric",
      day: "numeric",
    });
  } catch (e) {
    return null;
  }
}
