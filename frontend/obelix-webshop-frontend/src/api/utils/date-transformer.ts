import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, map } from "rxjs";

export const ISO_DATE_REGEX = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d{3})?Z?$/;

export function transformDates(obj: any): any {

    if (obj === null || obj === undefined || typeof obj !== 'object') {
        return obj;
    }

    if (obj instanceof Date) {
        return obj;
    }

    if (Array.isArray(obj)) {
        return obj.map(item => transformDates(item));
    }

    if (typeof obj === 'object') {
        const transformed: any = {};
        for (const key of Object.keys(obj)) {
            const value = obj[key];
            if (typeof value === 'string' && ISO_DATE_REGEX.test(value)) {
                transformed[key] = new Date(value);
            } else {
                transformed[key] = transformDates(value);
            }
        }
        return transformed;
    }

    return obj;
}

@Injectable()
export class DateInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        return next.handle(req).pipe(
            map(event => {
                if (event instanceof HttpResponse && event.body) {
                    return event.clone({ body: transformDates(event.body) });
                }
                return event;
            })
        );
    }
}
